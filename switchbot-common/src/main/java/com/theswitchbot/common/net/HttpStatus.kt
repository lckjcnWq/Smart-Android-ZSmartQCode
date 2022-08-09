package com.theswitchbot.common.net

enum class HttpStatus(val code:Int) {
    success(200),
    unauthorized(401),
    forbiden(403)
}

enum class BizStatus(val code:Int){
    unknownError(-999999),
    success(100),
    remarkNameExistError(120),
    groupNameExistError(121),
    nameExistError(122),
    sceneDeviceNotExist(152), //设备不存在
    inviteCodeNotExist (180 ),
    inviteCodeHasUsed(181),
    inviteCodeHasExpireTime(182),
    groupNotExist (183),
    joinedFamily (184),
    memberNotExist (185),
    sceneNoExist(186), //场景不存在
    hasJoinFamily (190)
}