package org.click.carservice.admin.model.notice.result;

import lombok.Data;
import org.click.carservice.db.domain.carserviceNotice;
import org.click.carservice.db.domain.carserviceNoticeAdmin;

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
    private carserviceNotice notice;

    /**
     * 通知管理员信息
     */
    private List<carserviceNoticeAdmin> noticeAdminList;

}
