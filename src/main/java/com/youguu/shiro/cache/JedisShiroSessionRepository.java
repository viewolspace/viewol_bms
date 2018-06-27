package com.youguu.shiro.cache;

import com.alibaba.fastjson.JSONObject;
import com.youguu.shiro.session.CustomSessionManager;
import com.youguu.shiro.session.SessionStatus;
import com.youguu.shiro.session.ShiroSessionRepository;
import com.youguu.shiro.utils.LoggerUtils;
import com.youguu.shiro.utils.SerializeUtil;
import org.apache.shiro.session.Session;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * Session 管理
 */
public class JedisShiroSessionRepository implements ShiroSessionRepository {
	public static final String REDIS_SHIRO_SESSION = "openms-shiro-session:";
	//这里有个小BUG，因为Redis使用序列化后，Key反序列化回来发现前面有一段乱码，解决的办法是存储缓存不序列化
	public static final String REDIS_SHIRO_ALL = "*openms-shiro-session:*";
	private static final int SESSION_VAL_TIME_SPAN = 18000;
	private static final int DB_INDEX = 1;

	private JedisManager jedisManager;

	@Override
	public void saveSession(Session session) {
		if (session == null || session.getId() == null){
			throw new NullPointerException("session is empty");
		}

		try {
			Session oldSession = this.getSession(session.getId());
			if(null != oldSession && null != oldSession.getAttribute("repeatData")){
				Map preMap = (Map) JSONObject.parse(oldSession.getAttribute("repeatData").toString());
				long preTime = Long.parseLong(preMap.get("time").toString());

				if(null != session.getAttribute("repeatData")){
					Map newMap = (Map) JSONObject.parse(session.getAttribute("repeatData").toString());
					long newTime = Long.parseLong(newMap.get("time").toString());

					if((newTime - preTime)<=0){
						return;
					}
				}

			}

			byte[] key = SerializeUtil.serialize(buildRedisSessionKey(session.getId()));


			//不存在才添加。
			if (null == session.getAttribute(CustomSessionManager.SESSION_STATUS)) {
				//Session 踢出自存存储。
				SessionStatus sessionStatus = new SessionStatus();
				session.setAttribute(CustomSessionManager.SESSION_STATUS, sessionStatus);
			}

			byte[] value = SerializeUtil.serialize(session);

           	//直接使用 (int) (session.getTimeout() / 1000) 的话，session失效和redis的TTL 同时生效
			getJedisManager().saveValueByKey(DB_INDEX, key, value, (int) (session.getTimeout() / 1000));
		} catch (Exception e) {
			LoggerUtils.fmtError(getClass(), e, "save session error，id:[%s]", session.getId());
		}
	}

	@Override
	public void deleteSession(Serializable id) {
		if (id == null) {
			throw new NullPointerException("session id is empty");
		}
		try {
			getJedisManager().deleteByKey(DB_INDEX,
					SerializeUtil.serialize(buildRedisSessionKey(id)));
		} catch (Exception e) {
//			LoggerUtils.fmtError(getClass(), e, "删除session出现异常，id:[%s]", id);
		}
	}


	@Override
	public Session getSession(Serializable id) {
		if (id == null)
			throw new NullPointerException("session id is empty");
		Session session = null;
		try {
			byte[] value = getJedisManager().getValueByKey(DB_INDEX, SerializeUtil.serialize(buildRedisSessionKey(id)));
			session = SerializeUtil.deserialize(value, Session.class);
		} catch (Exception e) {
//			LoggerUtils.fmtError(getClass(), e, "获取session异常，id:[%s]", id);
		}
		return session;
	}

	@Override
	public Collection<Session> getAllSessions() {
		Collection<Session> sessions = null;
		try {
			sessions = getJedisManager().AllSession(DB_INDEX, REDIS_SHIRO_SESSION);
		} catch (Exception e) {
//			LoggerUtils.fmtError(getClass(), e, "获取全部session异常");
		}

		return sessions;
	}

	private String buildRedisSessionKey(Serializable sessionId) {
		return REDIS_SHIRO_SESSION + sessionId;
	}

	public JedisManager getJedisManager() {
		return jedisManager;
	}

	public void setJedisManager(JedisManager jedisManager) {
		this.jedisManager = jedisManager;
	}
}
