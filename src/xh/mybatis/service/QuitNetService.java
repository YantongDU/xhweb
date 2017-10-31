package xh.mybatis.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xh.mybatis.bean.QuitNetBean;
import xh.mybatis.mapper.QuitNetMapper;
import xh.mybatis.tools.MoreDbTools;
import xh.mybatis.tools.MoreDbTools.DataSourceEnvironment;

public class QuitNetService {

    /**
     * 退网申请记录
     *
     * @return
     */
    public static List<QuitNetBean> selectAll(Map<String, Object> map) {
        SqlSession sqlSession = MoreDbTools.getSession(DataSourceEnvironment.slave);
        QuitNetMapper mapper = sqlSession.getMapper(QuitNetMapper.class);
        List<QuitNetBean> list = new ArrayList<QuitNetBean>();
        try {
            list = mapper.selectAll(map);
            sqlSession.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 申请进度查询
     *
     * @param id
     * @return
     */
    public static Map<String, Object> applyProgress(int id) {
        SqlSession sqlSession = MoreDbTools.getSession(DataSourceEnvironment.slave);
        QuitNetMapper mapper = sqlSession.getMapper(QuitNetMapper.class);
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            map = mapper.applyProgress(id);
            sqlSession.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 退网申请记录
     *
     * @return
     */
    public static String selectUserName(int id) {
        SqlSession sqlSession = MoreDbTools.getSession(DataSourceEnvironment.slave);
        QuitNetMapper mapper = sqlSession.getMapper(QuitNetMapper.class);
        String userName = "123";
        try {
            userName = mapper.selectUserName(id);
            sqlSession.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userName;
    }

    /**
     * 总数
     *
     * @return
     */
    public static int dataCount(Map<String, Object> map) {
        SqlSession sqlSession = MoreDbTools.getSession(DataSourceEnvironment.slave);
        QuitNetMapper mapper = sqlSession.getMapper(QuitNetMapper.class);
        int count = 0;
        try {
            count = mapper.dataCount(map);
            sqlSession.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return count;
    }

    /**
     * 退网申请
     *
     * @param bean
     * @return
     */
    public static int quitNet(QuitNetBean bean) {
        SqlSession sqlSession = MoreDbTools.getSession(DataSourceEnvironment.master);
        QuitNetMapper mapper = sqlSession.getMapper(QuitNetMapper.class);
        int result = 0;
        try {
            result = mapper.quitNet(bean);
            sqlSession.commit();
            result = 1;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            sqlSession.close();
        }
        return result;
    }

    /**
     * 管理方审核
     *
     * @param bean
     * @return
     */
    public static int checkedOne(QuitNetBean bean) {
        SqlSession sqlSession = MoreDbTools.getSession(DataSourceEnvironment.master);
        QuitNetMapper mapper = sqlSession.getMapper(QuitNetMapper.class);
        int result = 0;
        try {
            result = mapper.checkedOne(bean);
            sqlSession.commit();
            result = 1;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            sqlSession.close();
        }
        return result;
    }

    public static int checkedTwo(QuitNetBean bean) {
        SqlSession sqlSession = MoreDbTools.getSession(DataSourceEnvironment.master);
        QuitNetMapper mapper = sqlSession.getMapper(QuitNetMapper.class);
        int result = 0;
        try {
            result = mapper.checkedTwo(bean);
            sqlSession.commit();
            result = 1;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            sqlSession.close();
        }
        return result;
    }

    /**
     * 用户确认
     *
     * @param bean
     * @return
     */
    public static int sureFile(QuitNetBean bean) {
        SqlSession sqlSession = MoreDbTools.getSession(DataSourceEnvironment.master);
        QuitNetMapper mapper = sqlSession.getMapper(QuitNetMapper.class);
        int result = 0;
        try {
            result = mapper.sureFile(bean);
            sqlSession.commit();
            result = 1;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            sqlSession.close();
        }
        return result;
    }

}
