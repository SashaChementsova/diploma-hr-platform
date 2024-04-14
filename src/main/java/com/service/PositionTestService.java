package com.service;

import com.model.PositionTest;

import java.util.List;

public interface PositionTestService {
    public PositionTest addAndUpdatePositionTest(PositionTest positionTest);
    public List<PositionTest> getPositionTests();

    public PositionTest findPositionTestById(int id);
    public void deletePositionTest(int id);
}
