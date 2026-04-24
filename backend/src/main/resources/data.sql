INSERT INTO sys_user (username, password, name, department, role, status) VALUES 
('admin', 'e10adc3949ba59abbe56e057f20f883e', '管理员', '办公室', '超级管理员', 'enabled'),
('zhangxx', 'e10adc3949ba59abbe56e057f20f883e', '张XX', '司令部', '部门管理员', 'enabled'),
('lixx', 'e10adc3949ba59abbe56e057f20f883e', '李XX', '政治处', '普通用户', 'enabled'),
('wangxx', 'e10adc3949ba59abbe56e057f20f883e', '王XX', '后勤处', '普通用户', 'enabled');

INSERT INTO document (code, title, type, department, recipient, content, status) VALUES 
('2024-001', '关于开展年度训练考核的通知', '通知', '司令部', '各部门', '根据年度训练大纲要求，定于下月开展年度训练考核工作，请各部门做好准备。', 'pending'),
('2024-002', '关于申请采购办公设备的请示', '请示', '后勤处', '办公室', '因工作需要，申请采购办公电脑10台，打印机2台，预算共计5万元。', 'processing'),
('2024-003', '关于表彰优秀士兵的通报', '通报', '政治处', '各单位', '表彰在年度工作中表现突出的优秀士兵，希望全体官兵学习榜样，再创佳绩。', 'approved'),
('2024-004', '关于加强安全管理的决定', '决定', '办公室', '各部门', '为加强安全管理，特制定以下安全措施，请各部门严格执行。', 'rejected'),
('2024-005', '关于年度工作总结的报告', '报告', '政治处', '党委', '现将本年度工作总结报告如下，请审议。', 'approved');

INSERT INTO approval (title, type, applicant, applicant_department, content, status) VALUES 
('关于申请采购办公设备的请示', '采购审批', '张XX', '后勤处', '因工作需要，申请采购办公电脑10台，打印机2台，预算共计5万元。', 'pending'),
('关于年度训练计划的审批', '训练计划审批', '李XX', '司令部', '根据年度训练大纲要求，制定2024年度训练计划，请审批。', 'pending');

INSERT INTO meeting (title, type, date, time, location, participants, agenda, status) VALUES 
('党委扩大会议', '党委会议', '2024-01-15', '14:30 - 16:00', '一号会议室', '党委成员', '研究部署年度重点工作', 'processing'),
('周工作例会', '行政会议', '2024-01-16', '09:00 - 10:30', '二号会议室', '各部门负责人', '本周工作小结及下周计划安排', 'pending'),
('安全培训会议', '培训会议', '2024-01-14', '15:00 - 17:00', '培训室', '全体官兵', '消防安全知识培训', 'completed'),
('训练工作部署会', '业务会议', '2024-01-17', '10:00 - 11:30', '一号会议室', '训练骨干', '年度训练计划部署', 'pending');

INSERT INTO knowledge (name, type, size, folder, description) VALUES 
('2024年度训练计划.docx', 'Word文档', '256 KB', '训练资料', '2024年度训练计划'),
('安全管理制度.pdf', 'PDF文档', '1.2 MB', '规章制度', '安全管理制度汇编'),
('物资采购清单.xlsx', 'Excel表格', '128 KB', '后勤保障', '物资采购清单');

INSERT INTO monitor_task (title, department, person, deadline, priority, progress, status) VALUES 
('完成年度训练计划编制', '司令部', '李XX', '2024-01-20', 'urgent', 70, 'processing'),
('完善安全管理制度', '政治处', '王XX', '2024-01-18', 'important', 100, 'completed'),
('采购办公设备', '后勤处', '张XX', '2024-01-16', 'urgent', 30, 'pending'),
('组织年度考核', '司令部', '赵XX', '2024-01-14', 'normal', 100, 'completed');

INSERT INTO notification (user_id, title, content, type, `read`) VALUES 
('1', '新的公文待审批', '有一份新的公文需要您审批：关于申请采购办公设备的请示', 'info', FALSE),
('1', '会议提醒', '党委扩大会议将于今天14:30开始，请准时参加', 'info', FALSE),
('1', '任务督办', '您有一项督办任务即将到期：完成年度训练计划编制', 'warning', FALSE),
('2', '部门通知', '请及时提交本部门年度工作总结', 'info', FALSE),
('3', '审批通过', '您提交的请示已通过审批', 'success', FALSE);