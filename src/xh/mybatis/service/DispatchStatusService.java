package xh.mybatis.service;

import java.net.InetAddress;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import xh.mybatis.bean.DispatchBean;
import xh.mybatis.mapper.DispatchStatusMapper;
import xh.mybatis.tools.MoreDbTools;

public class DispatchStatusService {
	
	/**
	 * 调度台列表
	 * @return
	 */
	public static List<Map<String, String>> dispatchstatus(){
		SqlSession session = MoreDbTools.getSession(MoreDbTools.DataSourceEnvironment.slave);
		DispatchStatusMapper mapper = session.getMapper(DispatchStatusMapper.class);
		List<Map<String, String>> list=new ArrayList<Map<String,String>>();
		try {
			list = mapper.dispatchstatus();
			session.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		session.close();
		return list;
	}
	/**
	 * 调度台是否存在
	 * @param dstId
	 * @return
	 */
	public static int dispatchExists(int dstId){
		SqlSession session = MoreDbTools.getSession(MoreDbTools.DataSourceEnvironment.slave);
		DispatchStatusMapper mapper = session.getMapper(DispatchStatusMapper.class);
		int rsl=0;
		try {
			rsl=mapper.dispatchExists(dstId);
			session.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		session.close();
		return rsl;
	}
	/**
	 * 增加调度台
	 * @param bean
	 * @return
	 */
	public static int addDispatch(DispatchBean bean){
		SqlSession session = MoreDbTools.getSession(MoreDbTools.DataSourceEnvironment.master);
		DispatchStatusMapper mapper = session.getMapper(DispatchStatusMapper.class);
		int rsl=0;
		try {
			rsl=mapper.addDispatch(bean);
			session.commit();
			session.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		session.close();
		return rsl;
	}
	/**
	 * 修改调度台
	 * @param bean    
	 * @return
	 */
	public static int updateDispatch(DispatchBean bean){
		SqlSession session = MoreDbTools.getSession(MoreDbTools.DataSourceEnvironment.master);
		DispatchStatusMapper mapper = session.getMapper(DispatchStatusMapper.class);
		int rsl=0;
		try {
			rsl=mapper.updateDispatch(bean);
			session.commit();
			session.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		session.close();
		return rsl;
	}
	/**
	 * 删除调度台
	 * @param list
	 * @return
	 */
	public static int deleteDispatch(List<String> list){
		SqlSession session = MoreDbTools.getSession(MoreDbTools.DataSourceEnvironment.master);
		DispatchStatusMapper mapper = session.getMapper(DispatchStatusMapper.class);
		int rsl=0;
		try {
			rsl=mapper.deleteDispatch(list);
			session.commit();
			session.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		session.close();
		return rsl;
	}
	
	//ping 调度台
	public static boolean ping(String ipAddress) throws Exception {
        int  timeOut =  5000 ;  //超时应该在3钞以上        
        boolean status = InetAddress.getByName(ipAddress).isReachable(timeOut);     // 当返回值是true时，说明host是可用的，false则不可。
        return status;
    }
	public static void changePingStatus() throws Exception{
    	SqlSession session= MoreDbTools.getSession(MoreDbTools.DataSourceEnvironment.slave);
    	DispatchStatusMapper mapper = session.getMapper(DispatchStatusMapper.class);
    	List<Map<String, String>> list=new ArrayList<Map<String,String>>();
    	try {
			list = mapper.dispatchstatus();
			for(int i=0;i<list.size();i++){
				Map<String, String> map=list.get(i);
				int flag=0;
				if(ping(map.get("IP").toString())){
					flag=1;
				}else{
					flag=0;
				}
				Map<String, String> map2=new HashMap<String, String>();
				map2.put("dstId", map.get("dstId"));
				map2.put("flag", String.valueOf(flag));
				mapper.updateDispatchStatus(map2);
				
			}
			session.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
  }

}
