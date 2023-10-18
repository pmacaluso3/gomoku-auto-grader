package org.example.data;

import org.example.models.GradingBatch;

import java.util.List;

public interface GradingBatchRepository {
    public List<GradingBatch> findAll();

    public GradingBatch findById(int id);

    public GradingBatch create(GradingBatch gradingBatch);
}
