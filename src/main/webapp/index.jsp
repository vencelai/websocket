<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=ISO-8859-1">
</head>

<body>
	<meta charset="utf-8">
	<title>SenseLink WebSocket test</title>

	<script src="https://code.jquery.com/jquery-3.4.1.min.js"
		integrity="sha256-CSXorXvZcTkaix6Yvo6HppcZGetbYMGWSFlBw8HfCJo="
		crossorigin="anonymous">
		
	</script>

	<script language="javascript" type="text/javascript">
		var app_key = "38162d61dd6d2dca";
		var timestamp = "1569460051000"; // 後面要記得補3個0
		var sign = "fd595fcdfab477d68cb79063c78333b2";		
		var wsUri = "ws://192.168.30.70:9000/websocket/record/" + app_key + "/" + timestamp + "/" + sign;

		function init() {
			//output = document.getElementById("output");
			console.log("init success.");
		}

		function connect_senselink_ws() {
			websocket = new WebSocket(wsUri);
			websocket.onopen = function(evt) {
				onOpen(evt)
			};
			websocket.onmessage = function(evt) {
				onMessage(evt)
			};
			websocket.onerror = function(evt) {
				onError(evt)
			};
		}

		function onOpen(evt) {
			writeToScreen("Connected to Endpoint!");
			//doSend(textID.value);
		}

		function onMessage(evt) {
			//writeToScreen("Message Received: " + evt.data);
			console.log("Message Received: " + evt.data);
		}

		function onError(evt) {
			writeToScreen('<span style="color: red;">ERROR:</span> ' + evt.data);
		}

		function doSend(message) {
			writeToScreen("Message Sent: " + message);
			//websocket.send(message);
		}

		function writeToScreen(message) {
			var pre = document.createElement("p");
			pre.style.wordWrap = "break-word";
			pre.innerHTML = message;

			output.appendChild(pre);
		}

		window.addEventListener("load", init, false);

		$(document).ready(function() {
			$("#btnconnect").click(function() {
				//console.log("btn clicked");
				connect_senselink_ws();
			});

			$("#btndisconnect").click(function() {
				websocket.close();
				console.log("WebSocket is closed.");
			});
		});
		
	</script>

	<!-- <h2 style="text-align: center;">Hello World WebSocket Client</h2> -->
	<h3>SenseLink WebSocket test</h3>

	<br>
	
	<div>
		<button id="btnconnect">WS Connect</button>
		|
		<button id="btndisconnect">WS Close</button>		
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