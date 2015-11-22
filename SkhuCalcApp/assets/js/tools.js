
$(function(){//눈금을 위해
	var divs="";
	for(var i=0;i<20;i++) divs+="<div></div>";
	for(var i=0;i<13;i++){
		$(".scale:nth("+i+")").append(divs);
		for(var j=1;j<=20;j++){
			if((j-4)%5==0){
				$(".scale:nth("+i+") div:nth("+j+")").css('margin-top','-5px');
			}
			$(".scale:nth("+i+") div:nth("+j+")").css('left',''+(5*j)+'%');
		}
	}
	
});

//===============================================
// 안드로이드로 전송 할 데이터 모으기
var datas = {};
// category :

var category = {
	usrType : 1, //인문'society'/공학'engineering'
	applyType : 2, //수능1/일반2
};

function putCategory(type, value) {
	switch (type) {
	case category.usrType:
		datas.usr_type = value;
		break;
	case category.applyType:
		datas.apply_type = value;
		break;
	}
}

function nextPage() {// 인문계인지 공학계인지를 판별하여 적절한 곳으로 링크해준다.
	
	// 이 페이지는 수능으로밖에 올 수 없다.
	// 이 값은 2가 대입된다.

	if (datas.usr_type == "society")
		location.href = "#pagefour";
	else if (datas.usr_type == "engineering")
		location.href = "#pagefive";
}

function getAverage(){//학생부 평균을 구한다.
	if(datas.apply_type=='1'){//수능이면 이걸 할 필요가 없으므로 그냥 리턴.
		//showAlert("수능이라 학생부가 필요없음");
		return false;		
	}
	var yourAverage=0;
	var count=0;
	var curPage="pagesix";
	var error=false;
	
	datas.scores = "";
	$("#" + curPage + " input[name=scores]").each(function() {
		var temp = parseInt($(this).val());			
		if (isNaN(temp) == false && temp >= 1 && temp <= 9) {
			yourAverage += parseInt($(this).val());// 정상적인 숫자라면 더해준다.
			count++;// 정상적인 숫자의 개수를 세기 위함.
		}
		else{				
			error=true;
		}
	});
	
	if(error){
		//showAlert("학생부 등급값이 올바르지 않습니다.");
		return false;
	}
	else{
		datas.scores = Math.round(yourAverage / count * 100) / 100;
		return yourAverage=datas.scores;	
	}	
}

function submitData(curPage) {
	var temp;
	var error=false;//제대로된 학생부를 입력하지 않았을 경우
	
	datas.code = 1;
	if (datas.code == 1) {
		temp=Number($("#" + curPage + " input[name=kor]").val());
		(isNaN(temp)==false)? datas.kor =temp:datas.kor=0;
		
		temp=($("#" + curPage + " select[name=kor_type]").val());
		datas.kor_type =temp;
		
		temp=Number($("#" + curPage + " input[name=math]").val());
		(isNaN(temp)==false)? datas.math =temp:datas.math=0;
		
		temp=($("#" + curPage + " select[name=math_type]").val());
		datas.math_type =temp;
		
		temp=Number($("#" + curPage + " input[name=eng]").val());
		(isNaN(temp)==false)? datas.eng =temp:eng=0;
		
		temp=Number($("#" + curPage + " input[name=ss]").val());
		(isNaN(temp)==false)? datas.ss=temp:ss=0;		
		
		temp=($("#" + curPage + " select[name=ss_type]").val());
		datas.ss_type =temp;
		
		temp=getAverage(); if(temp==false) temp=0;
		datas.credit =temp;//학생부 신설!		
	}
	
		//showAlert("JSON / " + JSON.stringify(datas));		
		sendCode(JSON.stringify(datas));// 안드로이드에다가 보낸다.
		location.href = "#pageseven";
		
		if(datas.apply_type=='2'){//학생부 전형일경우
			var test=getAverage();		
			if (isNaN(test) == false) {//소수 2째자리까지 자르기.
				test = (Math.floor(test * 100)) / 100;
				$(".ui-block-c label").html("" + test);
			}			
		}
		else $(".ui-block-c label").html("");		
		
		setTimeout(function() {
			moveBars();
		}, 100);
}

function calcResult() {//수능 성적계산
	//(국어 백분위 * 0.4 + 영어 백분위 * 0.4 + 탐구/제2외국어/한문백문위 * 0.2) * 3
	var temp;
	var total = 0

	if (isNaN(Number(datas.kor)) == false)
		temp = datas.kor * 0.4;	//
	if ('b'==datas.kor_type){
		temp *= 1.1;
	}
	total += temp;

	
	if (isNaN(Number(datas.eng)) == false)
		temp = datas.eng * 0.4;
	total += temp;
	
	if (datas.usr_type == "engineering") {
		if (isNaN(Number(datas.math)) == false)
			temp = datas.math * 0.4;
		if (datas.math_type == 'b')
			temp *= 1.1;
		total += temp;
	}

	if (isNaN(Number(datas.ss)) == false)
		temp = datas.ss * 0.2;
	if (datas.usr_type == "engineering" && datas.ss_type == 'a')
		temp *= 1.05;

	total += temp;
	total *= 3;

	if (total >= 300)// 넘으면 300됨
		total = 300;

	total = Math.round(total * 100) / 100;
	return total;
}


function subCal(val){ //등급별 가산
	switch(val){
		case 1: return 12;
		case 2: return 10;
		case 3: return 9;
		case 4: return 8;
		case 5: return 7;
		case 6: return 6;
		case 7: return 4;
		case 8: return 2;
		default: return 0;	
	}
	
}
function calcResult2() {//학생부 성적계산
	var total = 0
	var curPage="pagesix";
	var error=false;
	//datas.scores = "";
	
	$("#" + curPage + " input[name=scores]").each(function() {
		var temp = parseInt($(this).val());			
		if (isNaN(temp) == false && temp >= 1 && temp <= 9) {
			total += subCal(parseInt($(this).val()));// 정상적인 숫자라면 더해준다.
		}
		else{				
			error=true;
		}
	});
	
	if(error){
		//showAlert("학생부 등급값이 올바르지 않습니다.");
		return false;
	}
	else{
		//datas.scores = Math.round(yourAverage / count * 100) / 100;
		//return yourAverage=datas.scores;	
		return (total+50+42); //50 : 교과 기본 점수, 42 : 비교과 만점
	}	
}


function trimNum(num){	
	if(num==0){
		return "000.00";
	}
	else{
		return num.toFixed(2);//소수점 고정
	}
}

//===================================결과===========================================
function moveBars() {
	function cal(temp) {
		if(temp<100) temp=100;			
		
		if(datas.apply_type==1){ //수능이면
			return (temp-100)/2;//수능은 만점이 300점이므로.	
		}
		else if(datas.apply_type==2){ //학생부이면
			return (temp-100)/4;//학생부는 만점이 500점이므로.	
		}	
	}	

	var yourPoint;
	var tempTable;
	
		
	
	if(datas.apply_type==1){ //수능이면
		yourPoint=Math.round(calcResult()*100)/100;//수능은 만점이 300점이므로.
	}
	
	else if(datas.apply_type==2){ //학생부이면
		yourPoint=Math.round((calcResult()+calcResult2())*100)/100;
		//수능은 만점이 300, 학생부는 만점이 200이므로...
	}	
	
	//showAlert('수능성적환산'+yourPoint);
	$('#point').html(Math.round(calcResult()*100)/100+"점"); //수능 점수 놓는곳
	$('#point2').html(Math.round(calcResult2()*100)/100+"점"); //학생부 점수 놓는곳
	
	
	
	if(datas.apply_type==1){//수능이면		
		tempTable=allData2;
		$("#pageseven #schoolscore").hide();//정시일 경우, 학생부 점수는 필요 없다.
	}
	else{
		tempTable=allData;
		$("#pageseven #schoolscore").show();
	}
	
	for (var i = 0; i < 13; i++) {		
		$(".bar_max:nth(" + i + ")").css('left',cal(tempTable[i][0][5]) + '%');
		$(".bar_min:nth(" + i + ")").css('left',cal(tempTable[i][1][5]) + '%');
		
		
		$(".bar_you:nth(" + i + ")").css('left', cal(yourPoint) + '%');
		
		
		$(".myScore:nth(" + i + ")").html(
				"<div style='color: red; position:absolute; left:30%; '>"+trimNum(tempTable[i][1][5])+"</div>"
				+"<div style='color: green; position:absolute; left:65%;'>"+trimNum(tempTable[i][0][5])+"</div>"
		);	
	}
	if(datas.usr_type=="society"){
		$("#nav1").show();
		$("#nav2").hide();
		showMe(1);
		$("#pageseven .myHeader").html("결과(인문계)");
	}
	else{
		$("#nav2").show();
		$("#nav1").hide();
		showMe(3);
		$("#pageseven .myHeader").html("결과 (공학계)");
	}
	//alert();
	//if()
	/*<div class="scale2">
	<span>100</span>
	<div class="bar_scale">min</div>
	<div class="bar_scale">you</div>
	<div class="bar_scale">max</div>
	<span>300</span>
	</div>*/
}
//===================================/결과===========================================






function resetAll(page) {	
	switch(page){
	case 0://모든 페이지 리셋
		$("input[type=text]").val("");
		$('#point').html("수능성적환산");
		$('#point2').html("학생부성적환산");
		for (var i = 0; i < 13; i++) {//바도 리셋
			$(".bar_min:nth(" + i + ")").css('left', '0%');
			$(".bar_max:nth(" + i + ")").css('left', '100%');
		}
		break;
	case 4:
		$("#pagefour input[type=text]").val("");
		break;
	case 5:
		$("#pagefive input[type=text]").val("");
		break;
	case 6:
		$("#pagesix input[type=text]").val("");
		break;			
	}
	showAlert("리셋되었습니다.");	

}

function receiveCode(json) {
	/*
	 * ReceiveCode는 안드앱에서 웹뷰로 받는 곳! MainActivity안에 @JavascriptInterface public
	 * void receiveCode(String json){...}
	 */

	var collection = eval(json);// 받은 자료를
	showResult(collection);// 그린다.
}
function showResult(collection) {//사용자에게 보여주는 부분
    applyType = 1;
    
    $("#res_score_register tbody").html("");

    for (var i = 0; i < collection.length; i++) {
        str = "";
        str += "<tr>";
        if(collection[i]["usr_type"] == "society")
            str += "<td>" + "인문" + "</td>";
        else
            str += "<td>" + "공학" + "</td>";

        
        if(collection[i]["math_type"]==null)
        	collection[i]["math_type"]="없음";
        
        //if(collection[i]["apply_type"] == 1){
        
        if(collection[i]["apply_type"] == 1)
            str += "<td>" + "수능" + "</td>";
        else
            str += "<td>" + "일반" + "</td>";        
            str += "<td>" + collection[i]["kor"] + "</td>";
            
            if(collection[i]["kor_type"]==null)	collection[i]["kor_type"]="없음";
            str += "<td>" + collection[i]["kor_type"] + "</td>";
            
            str += "<td>" + collection[i]["eng"] + "</td>";
            str += "<td>" + collection[i]["math"] + "</td>";
            str += "<td>" + collection[i]["math_type"] + "</td>";
            str += "<td>" + collection[i]["ss"] + "</td>";
            
            if(collection[i]["ss_type"] == "a")
            	str += "<td>" + "과학" + "</td>";
            else if(collection[i]["ss_type"] == "b")
                str += "<td>" + "사회" + "</td>";
            else if(collection[i]["ss_type"] == "c")
                str += "<td>" + "직업" + "</td>";
            else
                str += "<td>" + "없음" + "</td>";
            
            str += "<td>"+collection[i]["credit"]+"</td>";            
            
            str += "<td class='test' onclick=sendCode('" 
            	+ JSON.stringify({code : 6, _id : collection[i]["_id"]}) 
            	+ "')>선택</td>";
            str += "</tr>";
            //android에 요청

            //applyType = 1;
            $("#res_score_register tbody").append(str);
        //} 
        //showAlert(str);
    }
    
    $("#res_score_register").css({"display": "table"});    
    $(".test").css('background-color','#bbbbbb');
    
}

function sendCode(json) {// SendCode는 안드앱에다 json형식으로 데이터를 보내는것
	try {		
		window.SkhuCalcApp.receiveCode(json);
	} catch (e) {
		// alert(json); //{code: 3}줌
	}
}


function showAlert(str){//컴터는 alert, 안드로이드는 toast로 보여준다.
	try{
		window.SkhuCalcApp.showToast(str);
	}
	catch(e){
		alert(str);
	}
	
}

function page6nextPage(){
	if(getAverage()==false){
		showAlert("학생부 등급값이 올바르지 않습니다.");
	}
	else{
		nextPage();
	}
	
}

function showMe(choice){
	//alert(choice);
	switch(choice){
	case 1:
		$("#page7_s1").show();
		$("#page7_s2").hide();
		$("#page7_s3").hide();
		break;
	case 2:
		$("#page7_s1").hide();
		$("#page7_s2").show();
		$("#page7_s3").hide();
		break;
	case 3:
		$("#page7_s1").hide();
		$("#page7_s2").hide();
		$("#page7_s3").show();
		break;	
	}
}
