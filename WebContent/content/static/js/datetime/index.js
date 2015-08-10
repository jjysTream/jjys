//验证手机号
function istelphone(){ 
	var str = document.getElementById("uname").value;
	var upwd = document.getElementById("upwd").value;
	var reg = /^0?1[3|4|5|8][0-9]\d{8}$/;
	var tishi = document.getElementById("tishi");
	if(!reg.test(str)){
		tishi.innerHTML ="[输入正确的手机号码]";
		return false;
	}
	else if(upwd==null||upwd==""){
		tishi.innerHTML ="[密码不得为空]";
		return false;
	}
	else{
		return true;
	}
}
function isphoneandpwd(){
	var str = document.getElementById("runame").value;
	var pwd1 = document.getElementById("upwd1").value;
	var pwd2 = document.getElementById("upwd2").value;
	var tishi2 = document.getElementById("tishi2");
	var reg = /^0?1[3|4|5|8][0-9]\d{8}$/;
	if(!reg.test(str)){
//			var xmlHttp = new XMLHttpResquest();
//			xmlHttp.open("get", "${pageContext.request.contextPath}/user/checkutel.do?utel="+str);
//			xmlHttp.onreadystatechange = callback;
//			alert(xmlHttp.responseText);
//			xmlHttp.send(null);
		tishi2.innerHTML = "[输入正确的手机号码]";
		return false;
	}else if(pwd1==null||pwd1==""){		
		tishi2.innerHTML ="[密码不得为空]";
		return false;
	}
	else if(pwd1!=pwd2){
		tishi2.innerHTML ="[密码与确认密码不符]";
		return false;
	}
	else if(pwd1==pwd2){
		return true;
	}
//	else{
//		var url = "localhost:8081/micromarket/user/checkutel.do?utel="+str;
//		alert(url);
//		var xmlHttpRequest = new XMLHttpRequest();
//		xmlHttpRequest.open("GET", url, true);
//		if(xmlHttpRequest.readyState == 4){//Ajax引擎状态为成功
//            if(xmlHttpRequest.status == 200){//HTTP协议状态为成功
//                if(trim(xmlHttpRequest.responseText) != ""){
//                    userfield.focus();
//                }else{
//                    tishi2.innerHTML = "恭喜您，用户名可以使用。 ";
//                    return true;
//                }
//            }
//        }
//		return false;
//    };
}