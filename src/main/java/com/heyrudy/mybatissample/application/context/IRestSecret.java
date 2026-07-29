package com.heyrudy.mybatissample.application.context;

import com.heyrudy.mybatissample.application.context.IRestSecret.MockedRestSecret;
import com.heyrudy.mybatissample.application.context.IRestSecret.RestSecret;

public sealed interface IRestSecret
    permits RestSecret
    , MockedRestSecret {

    record RestSecret() implements IRestSecret {

    }

    record MockedRestSecret() implements IRestSecret {

    }
}
