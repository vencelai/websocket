package websocket;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@RestController
public class myController {
	
	@Autowired
	apiError apierror;
	
	// 接收 Android 丟過來的人員資料
	@RequestMapping(value = "/useradd", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
	public String Upload(MultipartHttpServletRequest request){
		String rval = "";
		
		//普通表单数据
		String icNumber = request.getParameter("icNumber");
		String jobNumber = request.getParameter("jobNumber");
		String mobile = request.getParameter("mobile");
		String name = request.getParameter("name");
		String remark = request.getParameter("remark");
		String groups = request.getParameter("groups");	
		
		if (icNumber == null || icNumber.length() == 0) {
			rval = apierror.getErrorMessage(101);
		} else if (jobNumber == null || jobNumber.length() == 0) {
			rval = apierror.getErrorMessage(102);
		} else if (mobile == null || mobile.length() == 0) {
			rval = apierror.getErrorMessage(103);
		} else if (name == null || name.length() == 0) {
			rval = apierror.getErrorMessage(104);
		} else if (remark == null || remark.length() == 0) {
			rval = apierror.getErrorMessage(105);
		} else if (groups == null || groups.length() == 0) {
			rval = apierror.getErrorMessage(106);
		} else {
			//獲取文件
			MultipartFile multipartFile = request.getFile("avatarFile");		
			if (!multipartFile.isEmpty()) {
				try {
					String uploadsDir = "/uploads/";
	                String realPathtoUploads =  request.getServletContext().getRealPath(uploadsDir);
//	                if(! new File(realPathtoUploads).exists()) {
//	                    new File(realPathtoUploads).mkdir();
//	                }
	                
	                if (!multipartFile.getContentType().equals("image/jpeg")) {
	                	rval = apierror.getErrorMessage(503);
	                } else {
	                	//保存文件
	                	multipartFile.transferTo(new File(realPathtoUploads + multipartFile.getOriginalFilename()));
	                	System.out.println("folder: " + realPathtoUploads);
	                	System.out.println("File full path: " + multipartFile.getOriginalFilename());
	                	// TODO Call SenseLink 添加人員 API	                	
	                	rval = apierror.getErrorMessage(200);
	                }
				} catch (IllegalStateException e) {
					rval = apierror.getErrorMessage(501);
				} catch (IOException e) {
					rval = apierror.getErrorMessage(502);
				}
			} else {
				rval = apierror.getErrorMessage(400);
			}
		}			
		
		return rval;
	}
	
	@RequestMapping(value = "/senselinkapi", method = RequestMethod.GET, produces="application/json;charset=UTF-8")
	public String senseLinkApi() {
		
		// 向 SenseLink 添加人員
		String url = "http://192.168.30.70/api/v1/user";
        
        Map<String, String> textMap = new HashMap<String, String>();
        //可以设置多个input的name，value
        textMap.put("icNumber", "1688");
        textMap.put("jobNumber", "");
        textMap.put("mobile", "111222333");
        textMap.put("name", "Api測試");
        textMap.put("remark", "");        
        textMap.put("groups", "1");        
        
        String app_key = "38162d61dd6d2dca";
		String app_secert = "7ba41f9a8ac98f2c16ee904bc7ea624c";
		String timestamp = getCurrentTimestamp();		
		String sign = "";
		try {
			sign = MD5Encoding(timestamp + "#" + app_secert);
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        textMap.put("app_key", app_key);
        textMap.put("sign", sign);
        textMap.put("timestamp", timestamp);               
        
        //设置file的name，路径
        Map<String, String> fileMap = new HashMap<String, String>();
        String fileName = "D:/hero.jpg";
        fileMap.put("avatarFile", fileName);        
        String contentType = "image/jpeg";
        String ret = formUpload(url, textMap, fileMap, contentType);
        
		return ret;
	}

	/**
	 * 上传
	 * 
	 * @param urlStr
	 * @param textMap
	 * @param fileMap
	 * @param contentType 没有传入文件类型默认采用application/octet-stream
	 *                    contentType非空采用filename匹配默认的图片类型
	 * @return 返回response数据
	 */
	@SuppressWarnings("rawtypes")
	public String formUpload(String urlStr, Map<String, String> textMap, Map<String, String> fileMap,
			String contentType) {
		String res = "";
		HttpURLConnection conn = null;
		// boundary就是request头和上传文件内容的分隔符
		String BOUNDARY = "---------------------------123821742118716";
		try {
			URL url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(30000);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			// conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows; U; Windows NT
			// 6.1; zh-CN; rv:1.9.2.6)");
			conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
			OutputStream out = new DataOutputStream(conn.getOutputStream());
			// text
			if (textMap != null) {
				StringBuffer strBuf = new StringBuffer();
				Iterator iter = textMap.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					String inputName = (String) entry.getKey();
					String inputValue = (String) entry.getValue();
					if (inputValue == null) {
						continue;
					}
					strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"\r\n\r\n");
					strBuf.append(inputValue);
				}
				out.write(strBuf.toString().getBytes());
			}
			// file
			if (fileMap != null) {
				Iterator iter = fileMap.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					String inputName = (String) entry.getKey();
					String inputValue = (String) entry.getValue();
					if (inputValue == null) {
						continue;
					}
					File file = new File(inputValue);
					String filename = file.getName();

					// 没有传入文件类型，同时根据文件获取不到类型，默认采用application/octet-stream
					contentType = new MimetypesFileTypeMap().getContentType(file);
					// contentType非空采用filename匹配默认的图片类型
					if (!"".equals(contentType)) {
						if (filename.endsWith(".png")) {
							contentType = "image/png";
						} else if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")
								|| filename.endsWith(".jpe")) {
							contentType = "image/jpeg";
						} else if (filename.endsWith(".gif")) {
							contentType = "image/gif";
						} else if (filename.endsWith(".ico")) {
							contentType = "image/image/x-icon";
						}
					}
					if (contentType == null || "".equals(contentType)) {
						contentType = "application/octet-stream";
					}
					StringBuffer strBuf = new StringBuffer();
					strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"; filename=\"" + filename
							+ "\"\r\n");
					strBuf.append("Content-Type:" + contentType + "\r\n\r\n");
					out.write(strBuf.toString().getBytes());
					DataInputStream in = new DataInputStream(new FileInputStream(file));
					int bytes = 0;
					byte[] bufferOut = new byte[1024];
					while ((bytes = in.read(bufferOut)) != -1) {
						out.write(bufferOut, 0, bytes);
					}
					in.close();
				}
			}
			byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
			out.write(endData);
			out.flush();
			out.close();
			// 讀取返回數據
			StringBuffer strBuf = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				strBuf.append(line).append("\n");
			}
			res = strBuf.toString();
			reader.close();
			reader = null;
		} catch (Exception e) {
			System.out.println("發送 Post 請求出錯。" + urlStr);
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
		return res;
	}
	
	/** MD5加密
	 * @param str
	 * @return
	 * @throws NoSuchAlgorithmException 
	 */
	public String MD5Encoding(String str) throws NoSuchAlgorithmException {
		String rval = "";
		
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		try {
			md5.update((str).getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte b[] = md5.digest();

		int i;
		StringBuffer buf = new StringBuffer("");

		for (int offset = 0; offset < b.length; offset++) {
			i = b[offset];
			if (i < 0) {
				i += 256;
			}
			if (i < 16) {
				buf.append("0");
			}
			buf.append(Integer.toHexString(i));
		}

		rval = buf.toString();		
		return rval;
	}
	
	/** 取得目前的 timestamp
	 * @return
	 */
	public String getCurrentTimestamp() {
		Date date= new Date();		 
		long time = date.getTime();
		return Long.toString(time);
	}

}
