package com.service;

import com.model.ResultTesting;

import java.util.List;

public interface ResultTestingService {
    public ResultTesting addAndUpdateResultTesting(ResultTesting resultTesting);
    public List<ResultTesting> getResultTestings();

    public ResultTesting findResultTestingById(int id);
    public void deleteResultTesting(int id);
}
