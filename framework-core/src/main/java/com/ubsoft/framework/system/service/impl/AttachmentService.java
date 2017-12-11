package com.ubsoft.framework.system.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ubsoft.framework.core.service.impl.BaseService;
import com.ubsoft.framework.system.entity.Attachment;
import com.ubsoft.framework.system.service.IAttachmentService;

@Service("attachmentService")
@Transactional
public class AttachmentService extends BaseService<Attachment> implements IAttachmentService {

	
}