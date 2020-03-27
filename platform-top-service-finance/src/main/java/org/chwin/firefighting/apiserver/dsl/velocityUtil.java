package org.chwin.firefighting.apiserver.dsl;

import java.io.StringWriter;

import lombok.SneakyThrows;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

public class velocityUtil {

	public static void main(String[] args) {
		String t="";
		String u_col=" '$uname','$usertype' ";
//创建context
		VelocityContext context = new VelocityContext();
//添加数据
		context.put( "uname", new String("艾龙") );
		context.put( "usertype", new String("维保公司") );
		System.out.println( getTmpltCalcRzt(u_col,context) );
	}

	//@depr
	@SneakyThrows
	public static String getTmpltCalcRztV2(String sqlTmplt, VelocityContext context) {
		// 初始化并取得Velocity引擎
		VelocityEngine ve = new VelocityEngine();
		ve.init();

		// 取得velocity的模版内容, 模板内容来自字符传

		// 输出流
		StringWriter writer = new StringWriter();

		// 转换输出   con,writer,loigtag,tmplt

		ve.evaluate(context, writer, "", sqlTmplt); // 关键方法

		String rzt = writer.toString();
		return rzt;
	}
	@SneakyThrows
	public static String getTmpltCalcRzt(String sqlTmplt, VelocityContext context) {
		// 初始化并取得Velocity引擎
				VelocityEngine ve = new VelocityEngine();
				ve.init();

				// 取得velocity的模版内容, 模板内容来自字符传

				// 输出流
				StringWriter writer = new StringWriter();

				// 转换输出   con,writer,loigtag,tmplt

				ve.evaluate(context, writer, "", sqlTmplt); // 关键方法

				String rzt = writer.toString();
				return rzt;
	}

}
