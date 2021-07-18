package com.polije.sipakost.aplikasikos;

public class DataKost {
    String idKos, namaKos, jenisKos, imageURL, luasKamar, alamat, fasKamar, fasKamarMandi, fasUmum, namaUser, telpUser;
    Double latitude, longitude;
    Integer jml, hargaPerBulan, minBayar;

    public DataKost() {
    }

    public DataKost(String idKos, String namaKos, String jenisKos, String imageURL, String luasKamar, String alamat, String fasKamar, String fasKamarMandi, String fasUmum, String namaUser, String telpUser, Double latitude, Double longitude, Integer jml, Integer hargaPerBulan, Integer minBayar) {
        this.idKos = idKos;
        this.namaKos = namaKos;
        this.jenisKos = jenisKos;
        this.imageURL = imageURL;
        this.luasKamar = luasKamar;
        this.alamat = alamat;
        this.fasKamar = fasKamar;
        this.fasKamarMandi = fasKamarMandi;
        this.fasUmum = fasUmum;
        this.namaUser = namaUser;
        this.telpUser = telpUser;
        this.latitude = latitude;
        this.longitude = longitude;
        this.jml = jml;
        this.hargaPerBulan = hargaPerBulan;
        this.minBayar = minBayar;
    }

    public String getNamaUser() {
        return namaUser;
    }

    public String setNamaUser(String namaUser) {
        this.namaUser = namaUser;
        return namaUser;
    }

    public String getTelpUser() {
        return telpUser;
    }

    public String setTelpUser(String telpUser) {
        this.telpUser = telpUser;
        return telpUser;
    }

    public String getLuasKamar() {
        return luasKamar;
    }

    public void setLuasKamar(String luasKamar) {
        this.luasKamar = luasKamar;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getFasKamar() {
        return fasKamar;
    }

    public void setFasKamar(String fasKamar) {
        this.fasKamar = fasKamar;
    }

    public String getFasKamarMandi() {
        return fasKamarMandi;
    }

    public void setFasKamarMandi(String fasKamarMandi) {
        this.fasKamarMandi = fasKamarMandi;
    }

    public String getFasUmum() {
        return fasUmum;
    }

    public void setFasUmum(String fasUmum) {
        this.fasUmum = fasUmum;
    }

    public String getIdKos() {
        return idKos;
    }

    public void setIdKos(String idKos) {
        this.idKos = idKos;
    }

    public String getNamaKos() {
        return namaKos;
    }

    public String setNamaKos(String namaKos) {
        this.namaKos = namaKos;
        return namaKos;
    }

    public String getJenisKos() {
        return jenisKos;
    }

    public String setJenisKos(String jenisKos) {
        this.jenisKos = jenisKos;
        return jenisKos;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String setImageURL(String imageURL) {
        this.imageURL = imageURL;
        return imageURL;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getJml() {
        return jml;
    }

    public void setJml(Integer jml) {
        this.jml = jml;
    }

    public Integer getHargaPerBulan() {
        return hargaPerBulan;
    }

    public int setHargaPerBulan(Integer hargaPerBulan) {
        this.hargaPerBulan = hargaPerBulan;
        return 0;
    }

    public Integer getMinBayar() {
        return minBayar;
    }

    public void setMinBayar(Integer minBayar) {
        this.minBayar = minBayar;
    }
}
