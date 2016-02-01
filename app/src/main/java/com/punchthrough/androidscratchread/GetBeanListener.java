package com.punchthrough.androidscratchread;

import com.punchthrough.bean.sdk.Bean;

/**
 * Provides callback for getBeanByName()
 */
public interface GetBeanListener {
    /**
     * Called on success
     */
    public void success(Bean bean);

    /**
     * Called on fail
     */
    public void fail();
}