package org.csu.mypetstore.service;


import org.csu.mypetstore.domain.Sequence;
import org.csu.mypetstore.persistence.SequenceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SequenceService {

    @Autowired
    private SequenceMapper sequenceMapper;

    public int getNextOrderId(){
        Sequence sequence = new Sequence("ordernum", -1);
        Sequence nSequence = sequenceMapper.getSequence(sequence);
        return nSequence.getNextId();
    }
}
