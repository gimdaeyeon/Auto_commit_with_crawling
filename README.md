# Auto_commit_with_crawling

## 1차 목표 달성!! 해야 할 일
- [ ] 예외상황 모두 적절하게 처리하기
- [ ] 매일 일정시간 마다 2 번정도 실행시켜 하루에 최소 1번의 push가 이루어지도록 스케쥴 설정
- [ ] 첫 번째 스케쥴이 성공했다면 다음 스케쥴은 실행시키지 않기
- [ ] 두 번째 스케쥴도 실패할 경우 메일이나 카카오톡으로 알림 보내기
- [ ] 날짜가 바뀌면 그 날의 성공, 실패여부 초기화하기(db저장 or 내부 변수)
- 코드 리펙터링하기

## 목표
1. 직접 컨트롤 하지 않고 크롤링을 이용하여 코드 생성, 커밋~푸쉬까지 하는 것 - 완료(2024-03-28)
2. aws나 구글 서버를 이용해서 서버에 올려 내 컴퓨터를 켜두지 않아도 자동으로 돌아가게 하는 것

## 실행 시퀀스
1. 크롤링할 사이트를 찾는다(프로그래머스 유력, 백준 등 코딩사이트)
2. 크롤링으로 내가 푼 문제의 정보를 가져온다.
	- 문제 번호
	- 문제 내용 및 제목
	- 내가 푼 코드
	- 문제 링크
3. ~~문제마다 형식에 맞게 클래스를 생성한다.~~
	- 문제마다 public이 붙지 않았거나 모종의 이유로 컴파일 오류가 발생될 수 있기 때문에
    - .java파일 대신에 md파일로 생성으로 변경
4. ~~가져온 정보들로 생성한 클래스에 형식에 맞게 코드를 작성한다.(파일 입출력 활용)~~
	- 클래스(.java)-> .md에 형식에 맞게 크롤링해서 가져온 내가 푼 문제에 대한 정보를 입력한다.
5. 커밋 메세지 형식에 맞게 작성하고 자동으로 commit 및 push될 수 있게 한다.
	- JGit 라이브러리를 사용해 Git 관련 동작들을 수행한다.
6. 위의 흐름을 매일 일정 시간마다 하루에 하나씩 실행한다.
7. 어떠한 이유로 인해서 실행중에 오류가 났다면 그 날의 다른 시간대에 한 번더 시도한다.
    - 한 번 더 시도했을 때도 오류가 발생했다면 이메일이나 카카오톡으로 알림을 보내도록 한다.(요금적인 문제가 없다면 카카오톡 우선 고려)

#### 특이사항
만약 내가 풀지 않은 문제 번호여서 접근할 수 없거나 없는 문제 번호일 경우
다음 문제 번호로 크롤링을 시도한다.

#### 고려사항
1. 내가 풀고 크롤링으로 등록한 문제정보를 DB에 등록할 것인가?(서버 요금 및 기술 한에 따라 고려)
	- DB에 저장하는 경우
		1. 문제를 푼 사이트
		2. 문제 번호
		3. 문제 제목 or 내용
		4. 내가 제출한 코드 
	- ~~DB에 저장하지 않는경우 출력으로 text파일에 최대한 간단하게 저장한다.~~
2. 모종의 이유로 인해 로그아웃이 된 경우 어떻게 해야할 지 
   - -> 크롤링 자체가 새로운 깨끗한 상태에서 브라우저를 실행시키기 때문에 매 프로세스를 실행시킬 때 로그인을 한다.

