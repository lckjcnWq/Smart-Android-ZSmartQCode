package com.theswitchbot.common.livedata

object LiveDataConst {
    const val updateDeviceBasicInfo = "updateDeviceBasicInfo"
    const val updateSensorActionInfo = "updateSensorActionInfo"
    //刷新添加单色时，灯带颜色改变
    const val updateStripAddColor = "updateStripAddColor"
    //灯带添加场景，预览效果
    const val stripPreviewScene = "stripPreviewScene"
    //刷新固件版本号
    const val sensorVersionUpdate = "sensorVersionUpdate"
    //排完序更新拓展按钮(遥控器)
    const val remoteRearrangeUpdate = "remoteRearrangeUpdate"
    //家庭分组，创建房间，更新房间数量
    const val updateRoomNumber = "updateRoomNumber"
    //遥控器震动更新状态
    const val remoteUpdateShark = "remoteUpdateShark"

    //更新家庭角色
    const val updateFamilyRole = "updateFamilyRole"
    //改变设备状态
    const val updateDeviceStatus = "updateDeviceStatus"
    //创建
    const val updateFamilyRemark = "updateFamilyRemark"
    //更新家庭名称
    const val updateFamilyName = "updateFamilyName"
    //更新成员邀请时间
    const val updateInvateTime = "updateInvateTime"
    //移除成员、解散家庭动作
    const val action = "action"
    //个人信息发生改变
    const val updateUserInfo = "updateUserInfo"

    //全部家庭列表的变化
    const val updateFromNetFamilyList = "updateFamilyList"
    //当前选中家庭的变化
    const val updateFromNetCurrentFamily = "updateCurrentFamily"
    //首页设备排序
    const val indexSortAction="indexSortAction"
    //用户登录成功
    const val userLoginSuccess = "userLoginSuccess"
    //用户退出登录
    const val userLogoutSuccess = "userLogOutSuccess"
    //回到首页
    const val goHomePage = "goHomePage"
    //
    const val updateNickName = "updateNickName"

    const val bulbDelayChange = "bulbDelayChange"
}