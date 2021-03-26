package Model;

import Interface.EndProcess;
import Interface.IModel;
import Utilitary.ModelState;

public class ModelBase implements IModel<ModelBase> {

  private byte m_isActive; // lapide

  private short m_arraySize; // tamanho do vetor

  public ModelBase() {
    setM_isActive(ModelState.ACTIVE);
  }

  // region GETTER & SETTER
  public short getM_arraySize() {
    return m_arraySize;
  }

  public void setM_arraySize(short m_arraySize) {
    this.m_arraySize = m_arraySize;
  }

  public byte getM_isActive() {
    return m_isActive;
  }

  public void setM_isActive(int m_isActive) {
    this.m_isActive = (byte) m_isActive;
  }

  public void setM_isActive(byte m_isActive) {
    this.m_isActive = m_isActive;
  }
  // endregion

  /**
   * Transcreve este objeto em um vetor de bytes
   */
  @Override
  public byte[] Writable() {
    // TODO Auto-generated method stub
    // ta aqui so para usar o comentario mesmo huehuehue
    return null;
  }

  @Override
  public ModelBase Readble(byte[] p_dataInBytes, EndProcess p_onComplete) {
    // TODO Auto-generated method stub
    // ta aqui so para usar o comentario mesmo huehuehue
    return null;
  }

  @Override
  public void Stringfy() {
    // TODO Auto-generated method stub
    
  }

}
