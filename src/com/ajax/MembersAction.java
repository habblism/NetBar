package com.ajax;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bean.Member;
import com.dao.impl.MemberDAOImpl;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class MembersAction implements Action {
	private static final String EMPTY_RESULT = "{members:[]}";

	@Override
	public String ajax(HttpServletRequest request, HttpServletResponse response) {

		MemberDAOImpl dao = new MemberDAOImpl();
		List<Member> members; 
		
		String start = request.getParameter("start");
		String end = request.getParameter("end");
		
		if(start.contains("-")) {
			return EMPTY_RESULT;
		}
		
		String filter = request.getParameter("filter");
		String filterValue = request.getParameter("filterValue");
		
		// 没有指定范围，则查询全部
		if(start==null||end==null) {
			members = dao.list();
		}
		// 若指定了范围，则查询范围内条目
		else {
			// 若给出了过滤条件，则加上过滤条件
			if(filter!=null&&filterValue!=null) {
				members = dao.list(Integer.parseInt(start),Integer.parseInt(end),filter,filterValue);
			}else {
				members = dao.list(Integer.parseInt(start),Integer.parseInt(end));
			}
		}
		
		if(members==null||members.size()==0) {
			return EMPTY_RESULT;
		}

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		SimpleDateFormat tf = new SimpleDateFormat("hh:mm:ss");
		
		JsonObject jsonObjectRoot = new JsonObject();
		JsonArray jsonArrayRoot = new JsonArray();
		members.forEach(member->{
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("会员帐号",member.getMemberNo());
			jsonObject.addProperty("姓名",member.getName());
			jsonObject.addProperty("状态",member.getStatus());
			jsonObject.addProperty("性别",member.getGender());
			jsonObject.addProperty("年龄",member.getAge());
			jsonObject.addProperty("帐号余额",member.getFunds());
			jsonObject.addProperty("最近登陆时间",df.format(member.getLastLoginDate()));
			jsonArrayRoot.add(jsonObject);
		});
		jsonObjectRoot.add("records", jsonArrayRoot);
		return jsonObjectRoot.toString();
	}

	private String format(Time duration) {
		// TODO Auto-generated method stub
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(duration);
		String hours = calendar.get(Calendar.HOUR)+"";
		String minutes = calendar.get(Calendar.MINUTE)+"";
		String seconds = calendar.get(Calendar.SECOND)+"";
		if(hours.length()==1) {
			hours = "0"+hours;
		}
		if(minutes.length()==1) {
			minutes = "0"+minutes;
		}
		if(seconds.length()==1) {
			seconds = "0"+seconds;
		}
		return hours+":"+minutes+":"+seconds;
	}
	

	public static void main(String[] args) {
		new MembersAction().ajax(null, null);
	}

}