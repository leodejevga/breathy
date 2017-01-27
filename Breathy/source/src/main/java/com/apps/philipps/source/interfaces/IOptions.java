package com.apps.philipps.source.interfaces;

import com.apps.philipps.source.Option;

import java.util.List;

/**
 * Created by leode on 27.01.2017.
 */

public interface IOptions extends IBootable {
    public List<Option> getOptions();
    public boolean setOption(int id, Object value);
}
