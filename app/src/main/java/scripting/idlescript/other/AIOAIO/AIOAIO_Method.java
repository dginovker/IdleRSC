package scripting.idlescript.other.AIOAIO;

class AIOAIO_Method {
  private String name;
  private boolean enabled;
  private Runnable action;

  public AIOAIO_Method(String name, boolean enabled, Runnable action) {
    this.name = name;
    this.enabled = enabled;
    this.action = action;
  }

  public String getName() {
    return name;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public void performAction() {
    if (action != null) {
      action.run();
    }
  }
}
