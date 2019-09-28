package websocket;

import org.springframework.stereotype.Component;

@Component
public class apiError {

	public String getErrorMessage(int errorCode) {
		String rval = "none";

		switch (errorCode) {
		case 101:
			rval = String.format("{code:%s,message:%s}", "101", "icNumber is empty}");
			break;
		case 102:
			rval = String.format("{code:%s,message:%s}", "102", "jobNumber is empty}");
			break;
		case 103:
			rval = String.format("{code:%s,message:%s}", "103", "mobile is empty}");
			break;
		case 104:
			rval = String.format("{code:%s,message:%s}", "104", "name is empty}");
			break;
		case 105:
			rval = String.format("{code:%s,message:%s}", "105", "remark is empty}");
			break;
		case 106:
			rval = String.format("{code:%s,message:%s}", "106", "groups is empty}");
			break;	
		case 200:
			rval = String.format("{code:%s,message:%s}", "200", "success}");
			break;
		case 400:
			rval = String.format("{code:%s,message:%s}", "400", "avatarFile not found}");
			break;	
		case 500:
			rval = String.format("{code:%s,message:%s}", "500", "Server error}");
			break;
		case 501:
			rval = String.format("{code:%s,message:%s}", "501", "IllegalStateException}");
			break;				
		case 502:
			rval = String.format("{code:%s,message:%s}", "502", "IOException}");
			break;	
		case 503:
			rval = String.format("{code:%s,message:%s}", "503", "avatarFile type error}");
			break;	
		}

		return rval;
	}
}
