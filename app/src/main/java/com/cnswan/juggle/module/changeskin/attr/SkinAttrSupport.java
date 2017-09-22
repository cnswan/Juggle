package com.cnswan.juggle.module.changeskin.attr;

import android.content.Context;
import android.util.AttributeSet;

import com.cnswan.juggle.module.changeskin.control.SkinConfig;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zhy on 15/9/23.
 */
public class SkinAttrSupport {
    public static List< SkinAttr> getSkinAttrs(AttributeSet attrs, Context context) {
        List< SkinAttr> skinAttrs = new ArrayList< SkinAttr>();
     SkinAttr skinAttr = null;
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            String attrName = attrs.getAttributeName(i);
            String attrValue = attrs.getAttributeValue(i);

            SkinAttrType attrType = getSupportAttrType(attrName);
            if (attrType == null) continue;

            if (attrValue.startsWith("@")) {
                int id = Integer.parseInt(attrValue.substring(1));
                String entryName = context.getResources().getResourceEntryName(id);

                if (entryName.startsWith(SkinConfig.ATTR_PREFIX)) {
                    //eg:(src,skin_my_bg)
                    skinAttr = new  SkinAttr(attrType, entryName);
                    skinAttrs.add(skinAttr);
                }
            }
        }
        return skinAttrs;

    }

    //查看属性名是不是枚举类中的一个类型;
    private static  SkinAttrType getSupportAttrType(String attrName) {
        for ( SkinAttrType attrType :  SkinAttrType.values()) {
            if (attrType.getAttrType().equals(attrName))
                return attrType;
        }
        return null;
    }

}
