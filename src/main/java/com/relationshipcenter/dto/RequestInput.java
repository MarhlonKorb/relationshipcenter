package com.relationshipcenter.dto;

import com.relationshipcenter.models.Request;
import com.relationshipcenter.models.Subject;

public record RequestInput(SubjectInput subject) {

    public Request toModel() {
        var subject = new Subject(this.subject.description());
        return new Request(subject);
    }
}
