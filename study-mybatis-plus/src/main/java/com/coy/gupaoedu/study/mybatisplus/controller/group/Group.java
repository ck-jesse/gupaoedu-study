package com.coy.gupaoedu.study.mybatisplus.controller.group;

import javax.validation.GroupSequence;
import javax.validation.groups.Default;

@GroupSequence(value = {GroupA.class, Default.class, GroupB.class})
public interface Group {

}
