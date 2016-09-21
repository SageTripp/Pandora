package com.zst.pandora.bean

import cn.bmob.v3.BmobObject
import cn.bmob.v3.datatype.BmobFile

/**
 * Created by zst on 2016-09-18  0018.
 * 描述:
 */

class Item(var name: String = "", var sketch: String = "", var preview: BmobFile?, var desc: BmobFile?, var apk: BmobFile?, var flag: String = "DEMO") : BmobObject()