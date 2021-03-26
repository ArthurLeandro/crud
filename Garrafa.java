import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Date;

class Garrafa {
  int id; // 4 bytes
  byte ativo; // 1 byte
  int tamanhoRegistro; // 4 bytes
  String marca; // 24 bytes
  char gasosa; // 2 bytes
  String fornecedor; // 24 bytes
  Date dataDeAquisicao; // 8 bytes
  float volume; // 4 bytes

  public Garrafa() {
    this.ativo = 1;
    this.tamanhoRegistro = 0;
    this.marca = "INDEFINIDA";
    this.gasosa = 'N';
    this.fornecedor = "INDEFINIDO";
    this.dataDeAquisicao = null;
    this.volume = 0.0f;

  }

  public Garrafa(byte ativo, int tamanhoRegistro, String marca, char gasosa, String fornecedor, Date dataDeAquisicao,
      float volume) {
    this.ativo = ativo;
    this.tamanhoRegistro = tamanhoRegistro;
    this.marca = marca;
    this.gasosa = gasosa;
    this.fornecedor = fornecedor;
    this.dataDeAquisicao = dataDeAquisicao;
    this.volume = volume;
  }

  // region GETTER SETTER

  public byte getAtivo() {
    return ativo;
  }

  public void setAtivo(byte ativo) {
    this.ativo = ativo;
  }

  public int getTamanhoRegistro() {
    return tamanhoRegistro;
  }

  public void setTamanhoRegistro(int tamanhoRegistro) {
    this.tamanhoRegistro = tamanhoRegistro;
  }

  public String getMarca() {
    return marca;
  }

  public void setMarca(String marca) {
    this.marca = marca;
  }

  public char getGasosa() {
    return gasosa;
  }

  public void setGasosa(char gasosa) {
    this.gasosa = gasosa;
  }

  public String getFornecedor() {
    return fornecedor;
  }

  public void setFornecedor(String fornecedor) {
    this.fornecedor = fornecedor;
  }

  public Date getDataDeAquisicao() {
    return dataDeAquisicao;
  }

  public void setDataDeAquisicao(Date dataDeAquisicao) {
    this.dataDeAquisicao = dataDeAquisicao;
  }

  public float getVolume() {
    return volume;
  }

  public void setVolume(float volume) {
    this.volume = volume;
  }

  // endregion

  public static Garrafa ByteArrayToObjeto(byte[] bytesDoObjeto) {
    Garrafa novaGarrafa = new Garrafa();
    ByteArrayInputStream aux = new ByteArrayInputStream(p_array);
    DataInputStream array = new DataInputStream(aux);
    try {
      novaGarrafa.setAtivo(array.readByte());
      novaGarrafa.setTamanhoRegistro(array.readInt());
      novaGarrafa.setId(array.readInt());
      novaGarrafa.setMarca(array.readUTF());
      novaGarrafa.setVolume(array.readFloat());
      novaGarrafa.setDataDeAquisicao(new Date(array.readLong()));
      novaGarrafa.setGasosa(array.readChar());
      novaGarrafa.setFornecedor(array.readUTF());
    } catch (Exception e) {
      // TODO: handle exception
    }
    return novaGarrafa;
  }

  public byte[] ObjetoToByteArray() {
    byte[] auxi = null;
    ByteArrayOutputStream array = new ByteArrayOutputStream();
    DataOutputStream corrente = new DataOutputStream(array);
    try {
      corrente.writeByte(getAtivo());
      setTamanhoRegistro(TamanhoDesseObjeto());
      corrente.writeInt(getTamanhoRegistro());
      corrente.writeInt(getId());
      corrente.writeUTF(getMarca());
      corrente.writeFloat(getVolume());
      corrente.writeLong(Date.parse(getDataDeAquisicao().toString()));
      corrente.writeChar(getGasosa());
      corrente.writeUTF(getFornecedor());
      auxi = array.toByteArray();
    } catch (Exception e) {
      // TODO: handle exception
      // nao achou nada entao libera o buffer e continua retornando nulo
      corrente.flush();
      auxi = null;
    }
    return auxi;
  }

  private int TamanhoDesseObjeto() {
    ByteArrayOutputStream array = new ByteArrayOutputStream();
    DataOutputStream corrente = new DataOutputStream(array);
    try {
      corrente.write(getAtivo());
      corrente.writeInt(getId());
      corrente.writeUTF(getMarca());
      corrente.writeFloat(getVolume());
      corrente.writeLong(Date.parse(getDataDeAquisicao().toString()));
      corrente.writeChar(getGasosa());
      corrente.writeUTF(getFornecedor());
    } catch (IllegalArgumentException er) {
      // nao deu pra converter a data para long
      corrente.flush();
      corrente.write(getAtivo());
      corrente.writeInt(getId());
      corrente.writeUTF(getMarca());
      corrente.writeFloat(getVolume());
      corrente.writeLong(getDataDeAquisicao().getTime());
      corrente.writeChar(getGasosa());
      corrente.writeUTF(getFornecedor());
    } catch (Exception e) {
      corrente.flush();
      // erro ao escrever entao limpa o buffer e deixa ele com tamanho 0
    }
    return corrente.size();
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

}