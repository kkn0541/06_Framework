const todoNo= new URLSearchParams(location.search).get("todoNo");


// 목록으로 버튼 동작  (메인페이지)

const goToList = document.querySelector('#goToList');

goToList.addEventListener("click",()=>{

  location.href="/";   //메인 페이지 요청  (GET방식)


});

// 완료 여부 변경 버튼

const completeBtn = document.querySelector(".complete-btn");

completeBtn.addEventListener("click", e=>{

  // 요소.dataset : data-*속성에 저장된 값 반환 
  // data-todo-no 세팅한 속성값 얻어오기 
  // data-todo-no (html -를 사용) ->dataset.todoNo (js카멜케이스)

  const todoNo = e.target.dataset.todoNo;

  let complete = e.target.innerText; //기존 완료 여부 값 얻어오기 

  //Y <-> N
  
  //삼항연산자 
  //조건 (complete ==='Y') 
  // TRUE면  'N' 으로 바꾸고 아니면 Y 로 
  complete = (complete ==='Y') ? 'N' : 'Y';

  //완료 여부 수정 요청하기 
  location.href
  =`/todo/changeComplete?todoNo=${todoNo}&complete=${complete}`;

 // http://localhost/todo/changeComplete?todoNo=1&complete=Y



  //console.log(todoNo);
 
  


});



// 수정 페이지 이동 버튼 

const updateBtn = document.querySelector("#updateBtn");

updateBtn.addEventListener("click",e =>{

location.href
="/todo/update?todoNo="+todoNo;

});







// 삭제버튼 

const deleteBtn = document.querySelector("#deleteBtn");

deleteBtn.addEventListener("click",e =>{

  location.href
  ="/todo/delete?todoNo="+todoNo;


});