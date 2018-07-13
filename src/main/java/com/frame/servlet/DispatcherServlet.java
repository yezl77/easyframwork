/**
 * Copyright (C), 2015-2018，XXX人名
 * FileName: DispatcherServlet
 * Author:   Administrator
 * Date:     2018/4/3 13:20
 * Description: 请求转换器
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.frame.servlet;



import com.frame.annotation.Controller;
import com.frame.bean.ModelAndView;
import com.frame.helper.ClassHelper;
import com.frame.helper.IocHelper;
import com.frame.util.ClassUtil;
import com.frame.util.ParamUtil;
import com.frame.util.PropertiesUtil;
import com.frame.util.RelationUtil;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 〈一句话功能简述〉<br>
 * 〈请求转换器〉
 *
 * @author Administrator
 * @create 2018/4/3
 * @since 1.0.0
 */
@WebServlet(urlPatterns = "/DispatcherServlet/*", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    Set<Class<?>> ControllerSet = new HashSet<Class<?>>();
    Map<Class<?>, Object> instanceMap = new HashMap<Class<?>, Object>();
    Map<Class<?>, Object> methodMap = new HashMap<Class<?>, Object>();

    public DispatcherServlet() {
        super();
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        ClassUtil.loadClass(ClassHelper.class.getName(), false);
        ClassUtil.loadClass(IocHelper.class.getName(), false);
        ControllerSet = ClassHelper.getControllerClass();
        instanceMap = IocHelper.getBeanMap();
        methodMap = RelationUtil.getMethodMap();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getRequestURI();
        String context = req.getContextPath();
        String path = url.replace(context, "");
        String methodPath = path.replace("/DispatcherServlet", "");
        Method method = (Method) methodMap.get(methodPath);
        String cPath = path.split("/")[2];
        Object controllerInstance = null;
        Object result = null;
        for (Class<?> controllerClass : ControllerSet) {
            Controller controller = (Controller) controllerClass.getAnnotation(Controller.class);
            String cValue = controller.value();
            if (cValue.equals(cPath)) {
                controllerInstance = instanceMap.get(controllerClass);
                break;
            }
        }
        try {
            result = ParamUtil.setParameter(controllerInstance, method, req,resp);
        } catch (Exception e) {
            log.error("DispatcherServlet.servlet:start-up web failure",e);
            throw new RuntimeException(e);
        }
        setResult(result, req, resp);
    }

    //执行方法后的返回值
    private void setResult(Object result,HttpServletRequest req, HttpServletResponse resp) {

        //如果有返回值
        if (result != null) {
            //如果返回的是JSP路径
            if (result instanceof String) {
                try {
                    req.getRequestDispatcher(PropertiesUtil.getJspPath()+result).forward(req, resp);
                } catch (Exception e) {
                    log.error("DispatcherServlet.setResult:return JSP failure!",e);
                    throw new RuntimeException(e);
                }
                return;
            }
            //如果返回的是ModelAndView类
            ModelAndView data = (ModelAndView) result;
            if (data.getPath() != null) {
                //返回JSP页面
                Map<String, Object> model = data.getModel();
                for(Map.Entry<String, Object> entry:model.entrySet()){
                    req.setAttribute(entry.getKey(), entry.getValue());
                }
                try {
                    req.getRequestDispatcher(PropertiesUtil.getJspPath()+data.getPath()).forward(req, resp);
                } catch (Exception e) {
                    log.error("DispatcherServlet.setResult:return JSP failure!",e);
                    throw new RuntimeException(e);
                }
            }else {
                //返回数据
                try {
                    Gson gson=new Gson();
                    resp.setContentType("application/json");
                    resp.setCharacterEncoding("UTF-8");
                    PrintWriter writer = resp.getWriter();
                    writer.write(gson.toJson(data.getModel()));
                    writer.flush();
                    writer.close();
                } catch (Exception e) {
                    log.error("DispatcherServlet.setResult:return DATA failure!",e);
                    throw new RuntimeException(e);
                }

            }
        }
    }

}

