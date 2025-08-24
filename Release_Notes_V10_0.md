# Release Notes v10.0 (2025-08-24)

## Major Changes
- **코드베이스 전환**
  - Java 기반 Legacy UI → **Kotlin + Jetpack Compose UI**로 전면 전환
  - **Clean Architecture** 패턴 도입: Presentation / Domain / Data 레이어 분리
  - **Hilt** 의존성 주입 구조 확립
- **문제 출제 구조 개선 준비**
  - ProblemSource / ProblemRouter 설계 적용 (랜덤/워크시트/웹소켓 확장 대비)
- **DB & 이미지 관리**
  - Room DB + ImageStorage 구조 유지
  - 원본/썸네일 분리 저장 + NoteDetail에서 Lazy Load 적용

## User-facing Features
- **UI 개선**
  - 곱셈/나눗셈 단계별 학습 화면 완전 리뉴얼
  - Carry / Borrow 과정을 단계별 입력 확인 가능
  - ConstraintLayout 기반 레이아웃 → 화면 크기별 대응 강화 (폰/태블릿/가로모드)
- **피드백 & 인터랙션**
  - 정답/오답 시 시각적 피드백 및 효과음 추가
  - `"참 잘했어요"` 오버레이 애니메이션 강화
  - 잘못된 입력 시 랜덤 오답 메시지 표시
- **도전 모드 준비**
  - Challenge 모드 UI 구조 반영 (출시 준비 단계)

## Improvements
- 입력 키패드 레이아웃 개선: 세로/가로 모드 모두 정렬 안정화
- UI 토큰(UiTokens) 기반으로 패딩, 마진, 간격 통일
- PanelStack / PresentationScaffold 구조 정리 → 대화면 대응 개선
- 코드 테스트 커버리지 확장: DivisionUiStateBuilderTest, PhaseEvaluatorTest 등 추가

## Testing
- 실기기 테스트: Pixel 6, Galaxy Tab A7, Galaxy S21
- 세로/가로/태블릿 환경에서 UI 스모크 테스트 완료
- 치명적 크래시 없음
- Compose UI 테스트 (ComposeTestRule)로 기본 플로우 검증 완료

