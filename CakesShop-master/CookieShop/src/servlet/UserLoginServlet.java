package servlet;

import model.User;
import service.UserService;
import servlet.CheckCodeServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "user_login",urlPatterns = "/user_login")
public class UserLoginServlet extends HttpServlet {
    private UserService uService = new UserService();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String ue = request.getParameter("ue");
        String password = request.getParameter("password");
        User user = uService.login(ue, password);
        String verifycode=request.getParameter("verifycode");
        HttpSession session=request.getSession();//创建session对象，获取服务器的共享数据
        String checkcode_server=(String)session.getAttribute("CHECKCODE_SERVER");//获取服务器端生成的验证码
        session.removeAttribute("CHECKCODE_SERVER");//确保验证码的一次性
        if(!verifycode.equalsIgnoreCase(checkcode_server)) {
            //验证码不正确
            //提示信息
            request.setAttribute("login_msg", "验证码错误！");
            //跳转登录页面
            request.getRequestDispatcher("/user_login.jsp").forward(request, response);
            return;
        }
        if(user==null) {

            request.setAttribute("failMsg", "用户名、邮箱或者密码错误，请重新登录！");
            request.getRequestDispatcher("/user_login.jsp").forward(request, response);
        }

        else {

            request.getSession().setAttribute("user", user);
            request.getRequestDispatcher("/user_center.jsp").forward(request, response);
            //3.验证码校验

        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
