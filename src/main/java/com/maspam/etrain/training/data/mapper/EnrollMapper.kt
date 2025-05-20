package com.maspam.etrain.training.data.mapper

import com.maspam.etrain.training.data.dto.CertificateDto
import com.maspam.etrain.training.data.dto.EnrollDto
import com.maspam.etrain.training.data.dto.KaryaNyataDto
import com.maspam.etrain.training.domain.model.CertificateModel
import com.maspam.etrain.training.domain.model.EnrollModel
import com.maspam.etrain.training.domain.model.KaryaNyataModel

fun EnrollDto.toEnrollModel(): EnrollModel {
    return EnrollModel(
        id = this.id,
        train = this.train,
        status = this.status,
        totalJp = this.totalJp,
        outDate = this.outDate,
        attandance = this.attandence,
        tPost = this.tPost,
        tKaryaNyata = this.tKaryaNyata,
        certificate = if(this.certificate?.isEmpty() == true) CertificateModel() else this.certificate?.first()?.toCertificateModel(),
        trainingDetail = this.trainingDetail?.toTrainingModel(),
        pLearn = this.pLearn,
        sLearn = this.sLearn,
        karyaNyataModel = if(this.karyanyata?.isEmpty() == true) KaryaNyataModel() else this.karyanyata?.first()?.toKaryaNyataModel(),
    )
}

fun CertificateDto.toCertificateModel(): CertificateModel {
    return CertificateModel(
        id = this.id,
        cert = this.cert,
    )
}

fun KaryaNyataDto.toKaryaNyataModel(): KaryaNyataModel {
    return KaryaNyataModel(
        id = this.id,
        status = this.status,
        att = this.att,
        user = this.user,
        enroll = this.enroll
    )
}