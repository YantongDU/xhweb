package xh.mybatis.mapper;

import java.util.List;
import java.util.Map;

import xh.mybatis.bean.QuitNetBean;

public interface QuitNetMapper {

    /**
     * 查询所有入网申请记录
     *
     * @return
     * @throws Exception
     */
    public List<QuitNetBean> selectAll(Map<String, Object> map) throws Exception;

    /**
     * 申请进度查询
     *
     * @param id
     * @return
     * @throws Exception
     */
    public Map<String, Object> applyProgress(int id) throws Exception;

    /**
     * 查询所有入网申请记录
     *
     * @return
     * @throws Exception
     */
    public String selectUserName(int id) throws Exception;

    /**
     * 总数
     *
     * @return
     * @throws Exception
     */
    public int dataCount(Map<String, Object> map) throws Exception;

    /**
     * 退网申请
     *
     * @param bean
     * @return
     * @throws Exception
     */
    public int quitNet(QuitNetBean bean) throws Exception;

    /**
     * 管理方审核
     *
     * @param bean
     * @return
     * @throws Exception
     */
    public int checkedOne(QuitNetBean bean) throws Exception;

    public int checkedTwo(QuitNetBean bean) throws Exception;

    public int checkedThree(QuitNetBean bean) throws Exception;

    public int checkedFour(QuitNetBean bean) throws Exception;
    public int checkedFive(QuitNetBean bean) throws Exception;
    public int checkedSix(QuitNetBean bean) throws Exception;
    public int checkedSeven(QuitNetBean bean) throws Exception;
    public int checkedEight(QuitNetBean bean) throws Exception;

}
