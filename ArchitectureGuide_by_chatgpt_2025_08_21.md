# SukSukMath 아키텍처 가이드 (완전판)

> **버전**: 2025-08-21  
> **대상 독자**: 6개월 뒤의 ‘나’ + 신규 기여자  
> **목표**: 폴 older, 단위/통합 테스트, 문제 생성→평가→UI매핑 전체 흐름을 한 문서로 복원 가능하도록.

---

## 0) 3분 요약 (Re-orientation Cheat Sheet)

1. **세션 시작**: `ProblemSource`가 문제를 만든다 → `ProblemSessionFactory`가 연속 세션(`Practice`/`Challenge`)을 준비한다.
2. **Domain 구성**:
   - Division: `DivisionPhaseSequenceProvider`가 `(TwoByOne|TwoByTwo|ThreeByTwo)` Creator 중 하나로 Phase 시퀀스를 만든다 → `DivisionPhaseEvaluator`가 정답 판정/단계 전환.
   - Multiplication: `MulPhaseSequenceProvider`가 `(TwoByTwo|ThreeByTwo)` Creator로 시퀀스 생성 → `MulPhaseEvaluator`가 판정.
3. **UI 갱신**: ViewModel이 Domain 상태 + 현재 입력을 `*UiStateMapper`로 변환 → 보드(`*Board*.kt`) + 입력패널이 그린다.
4. **피드백/효과**: `FeedbackProvider`가 메시지와 소리(`SoundEffects`)를 내보낸다. 완료 시 `CompletionOverlay`/`StampOverlay`.
5. **가로 레이아웃**: `DualPaneBoardScaffold`로 보드/패널 분리, `ScaledBoard`로 보드 스케일링.

---

## 1) 전체 디렉터리 지도 & 각 레이어 역할

```
app/src/main/java/com/shinjaehun/suksuk
├── AppNavHost.kt             # 화면 진입/경로 정의
├── DivisionScreenEntry.kt    # Division 화면 네비게이션 엔트리
├── MultiplicationScreenEntry.kt # Multiplication 화면 네비게이션 엔트리
├── MainActivity.kt           # Activity, NavHost 포함
├── MyApplication.kt          # Hilt Application
├── Routes.kt                 # 네비 라우트 상수
│
├── data                      # 문제 공급(소스), 세션 생성
│   ├── ChallengeSource.kt
│   ├── DefaultProblemSessionFactory.kt
│   ├── FixedOnceSource.kt
│   └── LocalRandomSource.kt
│
├── di                        # Hilt DI 모듈
│   ├── DivisionModule.kt
│   ├── DomainModule.kt
│   ├── MultiplicationModule.kt
│   ├── PresentationModule.kt
│   └── ProblemSourceModule.kt
│
├── domain                    # 비즈니스 규칙/상태/평가/시퀀스
│   ├── DomainStateFactory.kt
│   ├── OpType.kt
│   ├── Problem.kt
│   ├── ProblemSessionFactory.kt
│   ├── ProblemSource.kt
│   ├── SessionMode.kt
│   ├── division
│   │   ├── DivPracticeSnapshot.kt
│   │   ├── evaluator/DivisionPhaseEvaluator.kt
│   │   ├── info/{DivisionStateInfo, DivisionStateInfoBuilder}.kt
│   │   ├── model/{DivisionCell, DivisionPhase}.kt
│   │   └── sequence/
│   │       ├── {DivisionPhaseSequence, DivisionPhaseStep, DivisionPhaseSequenceProvider}.kt
│   │       └── creator/{DivisionPhaseSequenceCreator, TwoByOne..., TwoByTwo..., ThreeByTwo...}.kt
│   ├── eval/EvalResult.kt
│   ├── generator/ProblemRules.kt
│   ├── model/{DivisionDomainState, DomainState, MulDomainState}.kt
│   ├── multiplication
│   │   ├── MulPracticeSnapshot.kt
│   │   ├── evaluator/MulPhaseEvaluator.kt
│   │   ├── info/{MulStateInfo, MulStateInfoBuilder}.kt
│   │   ├── model/{MulCell, MulPhase}.kt
│   │   └── sequence/{MulPhaseSequence, MulPhaseStep, MulPhaseSequenceProvider}.kt
│   │       └── creator/{MulPhaseSequenceCreator, TwoByTwo..., ThreeByTwo...}.kt
│   └── pattern/{OperationPattern, PatternDetector}.kt
│
├── presentation              # ViewModel, Compose UI, 이펙트, 레이아웃
│   ├── MainScreen.kt
│   ├── ProblemPickerDialog.kt
│   ├── challenge/{ChallengeHostViewModel, ChallengeScreen, ...}
│   ├── common
│   │   ├── Highlight.kt
│   │   ├── effects/{AudioPlayer, LocalAudioPlayer, SoundEffects}.kt
│   │   ├── feedback/{CompletionOverlay, FeedbackEvent, FeedbackMessages, FeedbackOverlay, FeedbackProvider, FeedbackProviderImpl}.kt
│   │   ├── layout/{DualPaneBoardScaffold, FeedbackArea, PanelStack, PresentationScaffold, ScaledBoard, StampOverlay}.kt
│   │   └── notice/LandscapeNotSupportedPanel.kt
│   ├── component/{ColorfulCircleButton, InputPanel, NumberPad, OutlinedWhiteButton}.kt
│   ├── division/{Div*Text, DivisionBoard2By1And2By2, DivisionBoard3By2, DivisionScreen, DivisionViewModel, model/*}
│   └── multiplication/{Mul*Text, MultiplicationBoard, MultiplicationScreen, MultiplicationViewModel, model/*}
│
└── ui/theme/{Color, Theme, Type}.kt
```

### 레이어 요약
- **data**: 문제의 원천(랜덤/고정/챌린지)과 세션 팩토리.
- **domain**: 문제를 풀이 가능한 **단계 시퀀스(PhaseSequence)**로 만들고, 입력을 **평가(Evaluator)**하며, **DomainState**로 진실의 원천 유지.
- **presentation**: ViewModel이 Domain ↔ UI를 매핑하고, Compose가 그린다. 공통 효과/레이아웃/피드백 포함.
- **di**: 위 의존성 주입 정의.

---

## 2) 앱 흐름과 핵심 개념

### 2.1 세션/문제 생성
- **`ProblemSource`**: 문제 공급자 인터페이스.
  - `LocalRandomSource`: 로컬 랜덤 규칙(`ProblemRules`) 기반 생성.
  - `FixedOnceSource`: 테스트/데모용 고정 문제 공급.
  - `ChallengeSource`: 챌린지 모드 전용(난이도 성장/제약 조건).
- **`DefaultProblemSessionFactory`**: `ProblemSource`로부터 스트림/이터러블 세션을 구성. `SessionMode`(Practice/Challenge)에 따라 공급 정책을 조절.
- **`DomainStateFactory`**: `Problem` → `DomainState`(Division/Mul) 변환 시 진입점. 내부에서 `*StateInfoBuilder`와 `*PhaseSequenceProvider`를 사용해 상태를 준비.

### 2.2 도메인 상태와 단계(Phase) 시퀀스
- **`*StateInfoBuilder`**: 피제수/제수, 곱/피승수 자리수 등 **문제를 요약한 Info** 생성.
- **`*PhaseSequenceProvider`**: Info를 바탕으로 적절한 **Creator** 선택 → **`*PhaseSequence`** 산출.
- **`*PhaseSequence`**: 풀이 과정을 **정형화된 단계들(`*PhaseStep`)의 배열**로 보유.
- **`*PhaseStep`**: 현재 스텝의
  - `editableCells`: 이번 입력 타겟 셀들
  - `clearCells`: 스텝 진입 시 비워야 할 셀들
  - (선택) `totalLineTargets` / `subtractLineTargets` 등 UI 힌트용 메타
  - `expectedAnswer`: 이 스텝에서 정답으로 기대하는 값(한 자리 또는 두 자리)

> 이 구조 덕분에 **정답 판정 로직**은 스텝에 귀속되고, **UI 매퍼**는 스텝 집합을 누적해 화면을 그립니다.

### 2.3 평가(Evaluator)와 상태 전이
- **`DivisionPhaseEvaluator` / `MulPhaseEvaluator`**: UI의 입력 문자열을 해석해 `EvalResult` 반환.
  - `Correct(answer, nextStep)` / `Wrong(feedbackKey)` / `Incomplete` 등
- ViewModel은 `EvalResult`에 따라 **DomainState의 `currentStepIndex`** 등을 갱신하고, 필요한 경우 Snapshot 기록.

### 2.4 UI 매핑과 렌더링
- **`DivisionUiStateMapper` / `MulUiStateMapper`**:
  - (1) 전체 `PhaseSequence`를 0..현재스텝까지 누적하여 **확정값(inputs)**을 구성
  - (2) `currentInput`을 반영해 **편집중 셀(editable index)**을 표시
  - (3) 보조/합계/취소선 등 **시각 힌트** 반영
- **보드 컴포저블**:
  - Division: `DivisionBoard2By1And2By2`, `DivisionBoard3By2`
  - Multiplication: `MultiplicationBoard`
- **입력 패널**: `InputPanel`/`NumberPad` 가 숫자/지우기/엔터 이벤트를 발생 → ViewModel이 처리.
- **피드백/효과**:
  - `FeedbackProviderImpl`이 `FeedbackMessages`에서 메시지 선택
  - `LocalAudioPlayer` + `SoundEffects`로 긍/부정 사운드 재생
  - 완료 시 `CompletionOverlay` 또는 바닥 `StampOverlay` 노출

### 2.5 가로 레이아웃/스케일링
- **`DualPaneBoardScaffold`**: 좌측 보드/우측 패널 2-페인 레이아웃 제공(패딩, 간격, weight 세팅 포함).
- **`ScaledBoard`**: 보드 영역을 비율로 축소/확대. **주의**: 폰트 크기는 스케일에 따라 작아질 수 있으므로, 텍스트는 최소 크기 보장/라벨 축약 고려.

---

## 3) 주요 파일별 역할 (핵심만 빠르게)

### 3.1 네비게이션
- `Routes.kt`: 문자열 경로 상수.
- `AppNavHost.kt`: `NavHost` 선언, `MainScreen` 및 연산별 화면으로 라우팅.
- `DivisionScreenEntry.kt`, `MultiplicationScreenEntry.kt`: 인자 파싱/타입 안전한 네비 엔트리 래퍼.

### 3.2 Data 레이어
- `LocalRandomSource`: `ProblemRules`로 합당한 난이도 문제 생성.
- `FixedOnceSource`: 하나의 고정 문제 반환 → UI 테스트/데모에 사용.
- `ChallengeSource`: 챌린지 모드(연속 풀이, 오답 처리 정책 등)를 위한 공급자.
- `DefaultProblemSessionFactory`: Source를 묶어 세션(이터러블/시퀀스) 준비.

### 3.3 Domain 레이어
- `Problem.kt`: (피제수/제수, 곱/피승수 등) 연산 공통 모델.
- `DomainState.kt`: 공통 인터페이스(또는 sealed hierarchy)로 현재 스텝 인덱스, 시퀀스, 누적 입력 보관.
- **Division**
  - `DivisionStateInfo(Builder)`: 자리수/패턴/보조지표 분석.
  - `DivisionPhaseSequence(Provider/Creator)`: 패턴별 단계 생성기.
  - `DivisionPhaseEvaluator`: 스텝별 정답 판정.
  - `DivPracticeSnapshot`: 연습 모드 상태 스냅샷(진행도, 시도 기록 등).
- **Multiplication**
  - `MulStateInfo(Builder)`, `MulPhaseSequence(...)`, `MulPhaseEvaluator`
  - `MulPracticeSnapshot`
- `pattern/PatternDetector`: 자리수 기반으로 `OperationPattern` 산출(필요시 `DomainStateFactory`로 수렴 가능).

### 3.4 Presentation 레이어
- **ViewModel**
  - `DivisionViewModel`, `MultiplicationViewModel`:
    - 문제 로드 → `DomainStateFactory` 호출 → 입력 이벤트 처리(`onDigitInput`, `onClear`, `onEnter`) → `*UiStateMapper`로 상태 방출.
- **UI**
  - 보드: `DivisionBoard*`, `MultiplicationBoard`
  - 입력: `InputPanel`, `NumberPad`, `OutlinedWhiteButton`, `ColorfulCircleButton`
  - 레이아웃: `PresentationScaffold`, `DualPaneBoardScaffold`, `PanelStack`, `ScaledBoard`
  - 피드백: `FeedbackOverlay`, `CompletionOverlay`, `StampOverlay`
  - 이펙트: `SoundEffects`, `LocalAudioPlayer` (CompositionLocal로 주입)
- **챌린지**
  - `ChallengeHostViewModel` + `ChallengeScreen(Host)` + `ChallengeSnapshot`: 라운드 관리, 난이도 상승, 결과 집계.

### 3.5 DI 모듈 (예상 바인딩 맵)
- `ProblemSourceModule`: `ProblemSource` 구현체 바인딩(랜덤/고정/챌린지), `DefaultProblemSessionFactory` 제공.
- `DomainModule`: `DomainStateFactory`, `*StateInfoBuilder`, `*PhaseSequenceProvider`, `*PhaseEvaluator` 제공.
- `DivisionModule`/`MultiplicationModule`: 각 연산 전용 Creator 집합을 멀티바인딩으로 제공.
- `PresentationModule`: `FeedbackProvider`, `AudioPlayer` 등 UI 레벨 서비스 바인딩.

> 실제 바인딩 세부는 모듈 파일을 열어보면 된다. 여기선 구성 의도를 기억하기 위한 **지도** 제공.

---

## 4) 데이터 흐름 (입력 한 번을 예로)

### 4.1 Division 예: **93 ÷ 8**
```
[ProblemSource] → Problem(93, 8)
   ↓
[DomainStateFactory]
   ├─ DivisionStateInfoBuilder: 자리/패턴 분석
   ├─ DivisionPhaseSequenceProvider: TwoByOne creator 선택
   └─ DomainState 구성(currentStepIndex=0)
   ↓
[DivisionViewModel]
   ├─ UiStateMapper 로 초기 보드/보조/라인 상태 생성
   └─ 사용자가 "1" 입력 → currentInput="1"
       ↓ onEnter
       └─ DivisionPhaseEvaluator: 정답/오답/불완전 판정
            ├─ Correct → DomainState.currentStepIndex++
            └─ Wrong → FeedbackProvider가 메시지/사운드
       ↓
   UiStateMapper: 확정값 누적 + 다음 editable 셀 노출
   ↓
[Board + Overlays] 렌더링, 필요 시 Stamp/Completion 노출
```

### 4.2 Multiplication 예: **47 × 16**
- 1의 자리 곱, 받아올림(carry) 발생 가능. `MulPhaseStep`에 두 자리 입력(예: `InputMultiply1Total`)을 허용하는 단계가 존재.
- `MulUiStateMapper`가 **입력 2자리 분할(Tens/Ones)**, **합계 라인(totalLine)**, **보조 셀(carry)**, **하이라이트**를 스텝 누적 기준으로 표시.

---

## 5) UI 설계 포인트

### 5.1 테스트 태그 전략
- **권장 위치**: **이벤트 발신 컴포넌트**에 부여.
  - 예) `NumberPad` 숫자/엔터 버튼: `Modifier.testTag("numpad-<digit>")`, `numpad-enter`
  - 상위 `InputPanel`에는 **Panel 단위** 식별자만(`input-panel`) 부여(행동 검증은 버튼으로 수행).
- **오버레이/피드백**:
  - `FeedbackOverlay` 루트에 `feedback` tag (오답/정답 메시지 검증)
  - `CompletionOverlay` 이미지에는 `contentDescription="참 잘했어요"` (카운트/표시 여부 검증).

### 5.2 레이아웃/스케일링 팁
- `ScaledBoard` 사용 시 텍스트 최소 크기(`minFontSizeSp`)를 보장하거나, 큰 화면에선 `contentWidthFraction`으로 레이아웃 폭을 키워 글자 잘림 방지.
- 가로 모드에서 메시지 줄바꿈 이슈가 있으면 `maxWidth`/`padding`/`wrapContentWidth` 조합으로 측정 제약을 명시.

### 5.3 피드백 UX
- 정답: 파란색 계열 메시지, 짧은 사운드, **약한 진동(선택)**
- 오답: 빨간색 메시지, 짧은 사운드, **자동 닫힘 타이머**
- 완료: `CompletionOverlay`(클릭 시 다음 문제), 또는 하단 `StampOverlay` 가볍게 등장.

---

## 6) 확장 가이드 (Cookbook)

### 6.1 새 Division 패턴 추가(예: **4자리 ÷ 2자리**)
1. **Info**: `DivisionStateInfoBuilder`에 자리수 규칙 추가.
2. **Creator**: `FourByTwoDivPhaseSequenceCreator` 신설 → 단계 정의(`DivisionPhaseStep[]`).
3. **Provider**: `DivisionPhaseSequenceProvider`에 매핑 추가.
4. **Evaluator**: 기존 규칙으로 커버되면 무변경, 특수 단계 필요 시 분기 추가.
5. **UI**: `DivisionBoard*`에서 보드 셀/보조 셀 배치 확장.
6. **테스트**: 최소 1개 정상 시나리오 + 1개 오답 경로 + 경계(0, 받아내림 등) 케이스.

### 6.2 새 Multiplication 패턴(예: **3×3**)
- 위와 동일한 절차로 `Mul*` 네임스페이스에서 구현.

### 6.3 새 문제 소스 연동(예: 서버/웹소켓)
- `ProblemSource` 구현(`RemoteSource`) 작성 → `ProblemSourceModule`에 바인딩 → `DefaultProblemSessionFactory`가 우선순위/모드별 사용.

### 6.4 새 사운드/피드백 추가
- `SoundEffects`에 enum/리소스 추가 → `FeedbackProviderImpl`에서 맵핑.
- 테스트는 **사운드 호출 여부**를 인터페이스 목으로 검증.

### 6.5 챌린지 모드 규칙 변경
- `ChallengeHostViewModel`에서 라운드 성공/실패 정책 조정.
- `ChallengeSnapshot`에 통계 필드(연속 정답, 평균 시간 등) 추가.

---

## 7) 테스트 전략

### 7.1 도메인 유닛 테스트
- 대상: `*PhaseEvaluator`, `*StateInfoBuilder`, `*PhaseSequenceCreator`
- 검증: 입력 → `EvalResult` 정확성, 스텝 전이, 엣지(0, 받아올림/내림) 규칙.

### 7.2 UI 매퍼 테스트
- 대상: `DivisionUiStateMapper`, `MulUiStateMapper`
- 검증: **확정값/현재 입력/하이라이트/보조 라인**이 스텝 누적 규칙대로 표시되는지.

### 7.3 Compose UI 테스트
- **테스트 태그**를 통한 상호작용: `numpad-<digit>`, `numpad-enter`
- 피드백/오버레이 존재성 검사: `feedback` tag, `contentDescription="참 잘했어요"`
- 예시: `test_TwoByOne_UIFlow`, 곱셈 carry 단계 검증 등.

> **팁**: 시뮬레이터 회전에 따른 레이아웃 변화는 `DualPaneBoardScaffold` 파라미터(패딩/간격/weight)로 안정화.

---

## 8) 유지보수 팁 & 단순화 스위치

### 8.1 기억이 안 나도 되는 포인트
- 파일을 **이름 규칙**으로 찾으면 된다: `division/*` ↔ `multiplication/*`는 일대일 구조.
- "정보 생성(InfoBuilder) → 시퀀스 생성(SequenceProvider/Creator) → 평가(Evaluator) → 매핑(UiStateMapper)" 흐름만 복원하면 80% 해결.

### 8.2 나중에 단순화하고 싶다면
1. **`PatternDetector` 제거**: 자리수만 본다면 `DomainStateFactory` 안으로 흡수.
2. **InfoBuilder 축소**: 단순 자리수/특성만 반환하도록 변환(고급 메타는 Creator로 이관).
3. **Creator 통합**: 유사 패턴(2×1과 2×2)의 공통 스텝을 함수로 추출, Creator 수를 줄임.
4. **UI 매퍼 일원화**: 공통 로직을 베이스 매퍼로 승격, Division/Mul이 확장.

> 단, **테스트가 이미 빵빵**하므로, 구조 축소 시 테스트를 **먼저** 업데이트하고 적용.

---

## 9) Known Issues / 주의사항
- **스케일링 시 폰트 축소**: `ScaledBoard`로 전체 축소 시 폰트 가독성 저하 가능 → 최소 SP 보장/요소별 비율 조정.
- **가로 모드 메시지 줄바꿈**: 레이아웃 측정 폭 제약 명시(`fillMaxWidth` + 내부 `wrapContentWidth`/`padding`)로 해결.
- **테스트 태그 위치**: 상위 컨테이너가 아닌 **실제 클릭 컴포넌트**에 달 것.

---

## 10) 용어 사전 (Glossary)
- **Problem**: 한 번의 세로셈 대상(나눗셈/곱셈).
- **Session**: 문제 연속 풀이 단위(Practice/Challenge).
- **DomainState**: 현재 문제의 진실 소스. 스텝 인덱스, 시퀀스, 입력 기록 포함.
- **Phase/PhaseStep**: 풀이를 쪼갠 최소 단위/그 집합.
- **Evaluator**: 현재 스텝에 대한 정답 판정기.
- **UiStateMapper**: DomainState → 화면에 필요한 데이터로 변환.

---

## 11) 자주 하는 작업 체크리스트
- [ ] 새 패턴 추가 시 Creator/Provider/테스트 3종 세트 업데이트
- [ ] 입력 2자리 스텝이면 매퍼에서 Tens/Ones 분리 반영 확인
- [ ] Feedback 메시지 키와 사운드 매핑 동기화
- [ ] 가로 모드에서 메시지 길이/패딩 점검
- [ ] Play Store 릴리스 전 내부/폐쇄 테스트 플로우 재점검

---

## 12) 부록: 예제 스텝 스펙 (개념적)

### Division(2×1) 초기 3스텝 예시 (의사코드)
```kotlin
PhaseStep(
  editableCells = [QuotientHundreds?],
  clearCells    = [],
  expectedAnswer = "(없음 또는 skip)"
)
PhaseStep(
  editableCells = [QuotientOnes],
  clearCells    = [],
  expectedAnswer = "1"
)
PhaseStep(
  editableCells = [Multiply1Tens, Multiply1Ones],
  clearCells    = [AuxBorrowMark?],
  expectedAnswer = "(예: 16)"
)
```

### Multiplication(2×2) 일부 스텝 예시 (의사코드)
```kotlin
PhaseStep(
  editableCells = [InputMultiply1Total], // 2자리 입력
  expectedAnswer = "30" // 예: 5*6 = 30
)
PhaseStep(
  editableCells = [CarryP1Tens, OnesP1], // 매퍼에서 2자리 분해하여 각 셀 배치
  expectedAnswer = "3","0"
)
```

> 실제 셀 이름(`DivisionCell`, `MulCell`)은 코드 기준으로 사용.

---

## 13) 시작점: 어디부터 보면 되나?
1. **화면 흐름**: `AppNavHost.kt` → `MainScreen.kt` → 연산별 `*Screen`.
2. **도메인 생성**: `DomainStateFactory.kt` → `*StateInfoBuilder` → `*PhaseSequenceProvider` & Creator 확인.
3. **정답 판정**: `*PhaseEvaluator`에서 스텝별 규칙 읽기.
4. **화면 매핑**: `*UiStateMapper`를 보고 셀/하이라이트/라인 렌더링 규칙 파악.

---

## 14) 앞으로의 로드맵(메모)
- [ ] 문제 공급 라우터(ProblemRouter) 도입: 모드/난이도/리모트 유연성 강화.
- [ ] 다중 플레이/웹소켓 세션 실험(교실 실시간 모드).
- [ ] 연산 확장(3×3 곱셈, 4×2 나눗셈).
- [ ] 접근성: 키패드 포커스/스크린리더 라벨.

---

본 문서를 기준으로 **구조 복원 → 테스트 가동 → 기능 추가**까지 한 번에 이어갈 수 있습니다.  
필요 시 이 문서 최상단의 "3분 요약"만 읽고 시작해도 됩니다.

