package com.service;

import com.model.Result;

import java.util.List;

public interface ResultService {
    public Result addAndUpdateResult(Result result);
    public List<Result> getResults();

    public Result findResultById(int id);
    public void deleteResult(int id);
}
