package net.faxuan.tableProject;

/**
 * 学法base库考试成绩表实例
 */
public class ExamResult {
  private String id;
  private String user_account;
  private int exam_id;
  private String domain_code;
  private Double exam_result_score;
  private int is_pass;
  private int exam_join_num;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUser_account() {
    return user_account;
  }

  public void setUser_account(String user_account) {
    this.user_account = user_account;
  }

  public int getExam_id() {
    return exam_id;
  }

  public void setExam_id(int exam_id) {
    this.exam_id = exam_id;
  }

  public String getDomain_code() {
    return domain_code;
  }

  public void setDomain_code(String domain_code) {
    this.domain_code = domain_code;
  }

  public Double getExam_result_score() {
    return exam_result_score;
  }

  public void setExam_result_score(Double exam_result_score) {
    this.exam_result_score = exam_result_score;
  }

  public int getIs_pass() {
    return is_pass;
  }

  public void setIs_pass(int is_pass) {
    this.is_pass = is_pass;
  }

  public int getExam_join_num() {
    return exam_join_num;
  }

  public void setExam_join_num(int exam_join_num) {
    this.exam_join_num = exam_join_num;
  }
}
