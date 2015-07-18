/* 기존 라이브러리에서 왼쪽으로 swipe하는 액션을 핸들링 못하여 새로 만듦. */

var down=false;
var moveX=null;
var startX=null;

$(document)[0].ontouchstart = function(e) {
	startX = e.touches[0].clientX;
	down=true;
}	
$(document)[0].ontouchmove = function(e) {
	moveX = e.touches[0].clientX;
	if(moveX>startX+10 && down==true){
		e.preventDefault();
	}
	
	if(moveX>startX+70 && down==true){
		window.history.back();
		down=false;
	}
}	  