package com.project.sokdak2.api.domain.common;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;


@Getter
@Entity
@Table(name="tb_visits")
@NoArgsConstructor
public class Visits extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String ip;

    private String uri;

    private String method;

    @Builder
    public Visits(String ip, String uri, String method){
        this.ip = ip;
        this.uri = uri;
        this.method = method;
    }
}
