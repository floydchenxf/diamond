package com.floyd.diamond.biz.func;

import com.floyd.diamond.aync.Func;

/**
 * Created by floyd on 15-11-28.
 */
public class StringFunc implements Func<byte[], String> {
    @Override
    public String call(byte[] t) {
        return new String(t);
    }
}
