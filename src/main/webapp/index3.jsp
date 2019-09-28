<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>

<body>
	<meta charset="utf-8">
	<title>SenseLink WebSocket test 2</title>

	<script src="https://code.jquery.com/jquery-3.4.1.min.js"
		integrity="sha256-CSXorXvZcTkaix6Yvo6HppcZGetbYMGWSFlBw8HfCJo="
		crossorigin="anonymous">		
	</script>

	<script language="javascript" type="text/javascript">
	
		$(document).ready(function() {
			$("#btnjson").click(function() {				
				var t = JSON.parse('{"code":30000,"message":"push record","data":{"id":386,"userId":15,"name":"TomChen","type":1,"avatar":"5d79ac9f9812ae0001d7f082","direction":0,"verifyScore":0.963,"receptionUserId":0,"receptionUserName":"","groups":[{"id":1,"name":"默认组","type":1}],"deviceName":"STSK","sn":"SKP-200aa1d7c3c8d30a2733fbd41e35fc82","signDate":"2019-09-25","signTime":1569392130,"signAvatar":"5d8b06029812ae0001d7f36d","signBgAvatar":"5d8b06029812ae0001d7f36e","mobile":"139111111","icNumber":"","idNumber":"","jobNumber":"","remark":"","entryMode":1,"signTimeZone":"+08:00","docPhoto":"","latitude":0.0,"longitude":0.0,"address":"","location":"192.168.30.112"}}');
				//console.log(t['code'] + ", " + t['message']);
				//console.log(t['data'].name);
				console.log(t['data']['groups'][0].id);
				
				/*
				var myJSONObject = {"bindings": [  
			        {"nnn": "春天", "time": "标示", "add": "北京"},  
			        {"ircEvent": "好", "method": "方法", "regex": "上海"}  
			    ]};  
			  
			    alert("json对象取属性值\n"+myJSONObject.bindings[0].nnn);
			    
			  	///json对象转化成json字符串方法：  
			    var myJSONtext=myJSONObject.toJSONString();  
			    alert("json对象转化成字符串,toJSONString\n"+myJSONtext);  
			    var myJSONtext2=JSON.stringify(myJSONObject);  
			    alert("json对象转化成字符串,用全局的内置对象JSON.stringify\n"+myJSONtext2);
			    */
			});
		});
		
	</script>

	<br>
	
	<div>
		<button id="btnjson">json</button>
	</div>

	<!--
        <div style="text-align: center;">
            <form action="">
                <input onclick="send_message()" value="Send" type="button">
                <input id="textID" name="message" value="Hello WebSocket!" type="text"><br>
            </form>
        </div>
    -->

	<div id="output"></div>
</body>
</html>