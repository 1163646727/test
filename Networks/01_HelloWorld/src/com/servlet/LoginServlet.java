package com.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <BR> 处理登录请求<BR>
 *     servlet是Java对于web开发而产生的一项技术，可以讲servlet技术是Java专有的，它是服务器端的技术；
 *     1.继承HttpServlet，才能处理HTTP请求<BR>
 *     2.使用@WwbServlet,说明它要处理的请求路径<BR>
 * author: ChenQi <BR>
 * createDate: 2021/3/6 <BR>
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("+++++++doGet+++++++++++");
        doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("+++++++doPost+++++++++++");
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        if ("123".equals(username) && "456".equals(password) ) {
            // 返回登录成功 ChenQi
            resp.getWriter().write("login success");
        } else {
            // 返回登录失败 ChenQi
            resp.getWriter().write("login Failed");
        }
    }
}
