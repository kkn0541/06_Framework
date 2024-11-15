const tbody = document.querySelector("#memberList");
const selectbtn= document.querySelector("#selectMemberList");



  const memberList = () => {
    fetch("/board/memberList")
      .then(response => response.json())
      .then(memberList => {

        console.log(memberList);

        tbody.innerHTML = ""; //초기화

        for (let mem of memberList) {

          const tr = document.createElement("tr");
          const arr = ['memberNo', 'memberNickname', 'memberEmail', 'memberDelFl'];

          for (let key of arr) {
            const td = document.createElement("td");

            if (key === 'memberNo') {
              tr.append(td);
            }

            td.innerText = mem[key];
            tr.append(td);
          }
          tbody.append(tr);

        }






      });

  }


  selectbtn.addEventListener("click", () => {
    memberList();
});


//console.log("main.js loaded.");

//쿠키에 저장된 이메일 input 창에 뿌려놓기 
//로그인이 안된 경우 수행 

//쿠키에서 매개변수로 전달받은 key가 일치하는 value 얻어오기 함수 
const getCookie = (key) => {

  const cookies = document.cookie; // " k = v; k=v ; k=v...."

  // console.log(cookies); saveId=user01@kh.or.kr; testKey=testValue

  //cookie 문자열을 배영형태로 변환 
  const cookieList = cookies.split("; ") // "; " 기준으로 쪼갠다  그 후 배열형태로 반환해줌 
    .map(el => el.split("="));
  // "k=v" , "k=v"...
  // 배열.map(함수) : 배열의 각 요소를 이용해 함수 수행 후 
  //                  결과 값으로 새로운 배열을 만들어서 반환 
  // "k=v"  el 로 담고  el을  = 기준으로 쪼갬 ==> ["k","v"] 로 반환 

  //console.log(cookieList); // ['saveId', 'user01@kh.or.kr'] ['testKey', 'testValue']

  //배열 ->객체로변환( 그래야 다루기 쉽다 )

  const obj = {}; // 비어있는 객체 선언 

  for (let i = 0; i < cookieList.length; i++) {
    const k = cookieList[i][0];  //key 값 빼와서 k에 넣기 
    const v = cookieList[i][1]; //value 값 
    obj[k] = v; // 객체에 추가 
    // obj ["saveId"] = "user01@kh.or.kr";
    // obj ["testKey"] = "testValue";


  }
  //console.log(obj);
  return obj[key]; //매개변수로 전달받은 key 와
  // obj 객체에 저장된 key 가 일치하는 요소의 value값 반환 


}




//이메일 작성 input 태그 요소 
const loginEmail = document.querySelector("#loginForm input[name='memberEmail']"); //이메일 input 태그 번호 

if (loginEmail != null) { //로그인 창의 이메일 input태그가 화면에 존재할떄 

  // 쿠키 중 key 값이 "saveId" 인 요소의 value 얻어오기 
  const saveId = getCookie("saveId");

  if (saveId != undefined) {
    loginEmail.value = saveId; // 쿠키에서 얻어온 이메일 값을 input 요소의 value에 세팅 


    //아이디 저장 체크박스에 체크해 두기
    document.querySelector("input[name='saveId']").checked = true;


  }

}





console.log("main.js loaded");

//이메일 ㅡ 비밀번호 미작성 시 로그인 막기 

const loginForm = document.querySelector("#loginForm"); //form 태그 
// #loginForm 후손 input  name 값이 memberEmail
const loginPw = document.querySelector("#loginForm input[name='memberPw']"); //비밀번호 input 태그 

// #loginForm 이 화면에 존재할때 ( == 로그인 상태 아닐 때  )
// -> 타임리프에 의해 로그인 되었다면 #loginForm 요소는 화면에 노출되지 않음 
// -> 로그인 상태 일대 loginForm 을 이용한 코드가 수행된다면 
// -> 콘솔창에 error 발생 


if (loginForm != null) {
  //제출 이벤트 발생 시 
  loginForm.addEventListener("submit", e => {

    //이메일 미작성 
    if (loginEmail.value.trim().length === 0) {
      alert("이메일을 작성해주세요")
      e.preventDefault(); //
      loginEmail.focus(); //
      return;

    }


    //비밀번호 미작성 

    if (loginPw.value.trim().length === 0) {
      alert("비밀번호를  작성해주세요")
      e.preventDefault(); //
      loginEmail.focus(); //
      return;

    }

  });
}