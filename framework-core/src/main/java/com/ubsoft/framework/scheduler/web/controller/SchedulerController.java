package com.ubsoft.framework.scheduler.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ubsoft.framework.scheduler.service.ITaskService;
import com.ubsoft.framework.web.controller.BaseController;

@RequestMapping("/scheduler")
@Controller
public class SchedulerController extends BaseController {
	@Autowired
	ITaskService taskService;
	
}