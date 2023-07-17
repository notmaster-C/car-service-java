package org.click.carservice.admin.model.notice.result;

import lombok.Data;
import org.click.carservice.db.domain.CarServiceNotice;
import org.click.carservice.db.domain.CarServiceNoticeAdmin;

import java.io.Serializable;
import java.util.List;

/**
 * 通知详情
 *
 * @author click
 */
@Data
public class NoticeReadResult implements Serializable {

    /**
     * 通知信息
     */
    private CarServiceNotice notice;

    /**
     * 通知管理员信息
     */
    private List<CarServiceNoticeAdmin> noticeAdminList;

}
