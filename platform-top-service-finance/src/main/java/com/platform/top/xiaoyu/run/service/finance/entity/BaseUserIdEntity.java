package com.platform.top.xiaoyu.run.service.finance.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.top.xiaoyu.rearend.model.entity.PlatformIdBaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 *  实体类
 *
 * @author coffey
 */
@Getter
@Setter
@MappedSuperclass
public abstract class BaseUserIdEntity<PK extends Long> extends PlatformIdBaseEntity<PK> {
	/*** userId */
	public static final String P_USER_ID = "user_id";

	/**
	 * 用户ID
	 */
	@TableField(value = P_USER_ID)
	@Column(name = P_USER_ID, updatable = false)
	private Long userId;

}
