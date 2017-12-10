package com.ajax;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bean.Computer;
import com.dao.impl.ComputerDAOImpl;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class ComputerStatusAction implements Action {
	private static final String EMPTY_RESULT = "{records:[]}";
	private static final String UPDATE_FAIL = "更新失败！";

	@Override
	public String ajax(HttpServletRequest request, HttpServletResponse response) {
		String type = request.getParameter("type");
		switch(type) {
		case "query":
			return handleQuery(request,response);
		case "update":
			return handleUpdate(request,response);
		default :
			return EMPTY_RESULT;
		}
	}
	
	

	private String handleUpdate(HttpServletRequest request, HttpServletResponse response) {
		ComputerDAOImpl dao = new ComputerDAOImpl();
		String colomnName= request.getParameter("columnName");
		String value = request.getParameter(colomnName);
		String computerNo = request.getParameter("computerNo");
		try {
			value = URLDecoder.decode(request.getParameter(colomnName),"UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(colomnName.equals("comment")) {
			if(dao.updateComment(Long.parseLong(computerNo), value)) {
				return "{\"result\":\""+value+"\"}";
			}
			return UPDATE_FAIL;
		}else if(colomnName.equals("status")) {
			if(dao.updateStatus(Long.parseLong(computerNo), value)) {
				return "{\"result\":\""+value+"\"}";
			}
			return UPDATE_FAIL;
		}
		return UPDATE_FAIL;
	}



	private String handleQuery(HttpServletRequest request, HttpServletResponse response) {
		ComputerDAOImpl dao = new ComputerDAOImpl();
		List<Computer> computers;
		String start = request.getParameter("start");
		String end = request.getParameter("end");

		if (start.contains("-")) {
			return EMPTY_RESULT;
		}

		String filter = request.getParameter("filter");
		String filterValue = request.getParameter("filterValue");

		// 没有指定范围，则查询全部
		if (start == null || end == null) {
			computers = dao.list();
		}
		// 若指定了范围，则查询范围内条目
		else {
			{
				// 若给出了过滤条件，则加上过滤条件
				if (filter != null && filterValue != null) {
					computers = dao.list(Integer.parseInt(start), Integer.parseInt(end), filter, filterValue);
				} else {
					computers = dao.list(Integer.parseInt(start), Integer.parseInt(end));
				}
			}
		}

		if (computers == null || computers.size() == 0) {
			return EMPTY_RESULT;
		}

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		SimpleDateFormat tf = new SimpleDateFormat("hh:mm:ss");

		JsonObject jsonObjectRoot = new JsonObject();
		JsonArray jsonArrayRoot = new JsonArray();
		computers.forEach(computer -> {
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("computerNo", computer.getComputerNo());
			jsonObject.addProperty("status", computer.getStatus());
			jsonObject.addProperty("comment", computer.getComment());
			jsonArrayRoot.add(jsonObject);
		});
		jsonObjectRoot.add("computerStatus", jsonArrayRoot);

		return jsonObjectRoot.toString();


	}



	private String toChinese(String status) {
		if (status.equals("idle")) {
			return "空闲";
		} else if (status.equals("busy")) {
			return "正在使用";
		} else if (status.equals("broken")) {
			return "待维修";
		} else
			return "未知";
	}

	public static void main(String[] args) {
		new ComputerStatusAction().ajax(null, null);
	}

}
