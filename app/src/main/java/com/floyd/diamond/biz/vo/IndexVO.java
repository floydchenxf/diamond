package com.floyd.diamond.biz.vo;

import com.floyd.diamond.biz.vo.mote.MoteInfoVO;

import java.util.List;

/**
 * Created by floyd on 15-12-12.
 */
public class IndexVO {

    /**
     * 类目
     */
    public List<IndexItemVO> categoryPics;

    /**
     * 广告
     */
    public List<AdvVO> advertList;

    /**
     * 女模特
     */
    public List<MoteInfoVO> moteVOs;
}
