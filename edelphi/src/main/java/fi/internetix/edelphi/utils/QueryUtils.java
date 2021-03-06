package fi.internetix.edelphi.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import fi.internetix.edelphi.EdelfoiStatusCode;
import fi.internetix.edelphi.dao.querydata.QueryQuestionCommentDAO;
import fi.internetix.edelphi.dao.querydata.QueryQuestionMultiOptionAnswerDAO;
import fi.internetix.edelphi.dao.querydata.QueryQuestionNumericAnswerDAO;
import fi.internetix.edelphi.dao.querydata.QueryQuestionOptionAnswerDAO;
import fi.internetix.edelphi.dao.querydata.QueryQuestionOptionGroupOptionAnswerDAO;
import fi.internetix.edelphi.dao.querydata.QueryQuestionTextAnswerDAO;
import fi.internetix.edelphi.dao.querydata.QueryReplyDAO;
import fi.internetix.edelphi.dao.querylayout.QueryPageDAO;
import fi.internetix.edelphi.dao.querylayout.QueryPageSettingDAO;
import fi.internetix.edelphi.dao.querylayout.QueryPageSettingKeyDAO;
import fi.internetix.edelphi.dao.querylayout.QuerySectionDAO;
import fi.internetix.edelphi.dao.querymeta.QueryFieldDAO;
import fi.internetix.edelphi.dao.querymeta.QueryNumericFieldDAO;
import fi.internetix.edelphi.dao.querymeta.QueryOptionFieldDAO;
import fi.internetix.edelphi.dao.querymeta.QueryOptionFieldOptionDAO;
import fi.internetix.edelphi.dao.querymeta.QueryOptionFieldOptionGroupDAO;
import fi.internetix.edelphi.dao.querymeta.QueryScaleFieldDAO;
import fi.internetix.edelphi.dao.querymeta.QueryTextFieldDAO;
import fi.internetix.edelphi.dao.resources.QueryDAO;
import fi.internetix.edelphi.domainmodel.panels.Panel;
import fi.internetix.edelphi.domainmodel.panels.PanelStamp;
import fi.internetix.edelphi.domainmodel.querydata.QueryQuestionComment;
import fi.internetix.edelphi.domainmodel.querydata.QueryQuestionMultiOptionAnswer;
import fi.internetix.edelphi.domainmodel.querydata.QueryQuestionNumericAnswer;
import fi.internetix.edelphi.domainmodel.querydata.QueryQuestionOptionAnswer;
import fi.internetix.edelphi.domainmodel.querydata.QueryQuestionOptionGroupOptionAnswer;
import fi.internetix.edelphi.domainmodel.querydata.QueryQuestionTextAnswer;
import fi.internetix.edelphi.domainmodel.querydata.QueryReply;
import fi.internetix.edelphi.domainmodel.querylayout.QueryPage;
import fi.internetix.edelphi.domainmodel.querylayout.QueryPageSetting;
import fi.internetix.edelphi.domainmodel.querylayout.QueryPageSettingKey;
import fi.internetix.edelphi.domainmodel.querylayout.QueryPageType;
import fi.internetix.edelphi.domainmodel.querylayout.QuerySection;
import fi.internetix.edelphi.domainmodel.querymeta.QueryField;
import fi.internetix.edelphi.domainmodel.querymeta.QueryNumericField;
import fi.internetix.edelphi.domainmodel.querymeta.QueryOptionField;
import fi.internetix.edelphi.domainmodel.querymeta.QueryOptionFieldOption;
import fi.internetix.edelphi.domainmodel.querymeta.QueryOptionFieldOptionGroup;
import fi.internetix.edelphi.domainmodel.querymeta.QueryScaleField;
import fi.internetix.edelphi.domainmodel.resources.Query;
import fi.internetix.edelphi.domainmodel.users.User;
import fi.internetix.edelphi.i18n.Messages;
import fi.internetix.edelphi.pages.panel.admin.report.util.QueryReportPageThesis;
import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.RequestContext;

public class QueryUtils {

  
  public static void appendQueryPageThesis(RequestContext requestContext, QueryPage queryPage) {
    @SuppressWarnings("unchecked")
    Map<Long, QueryReportPageThesis> thesises = (Map<Long, QueryReportPageThesis>) requestContext.getRequest().getAttribute("queryPageThesises");
    if (thesises == null) {
      thesises = new HashMap<Long, QueryReportPageThesis>();
      requestContext.getRequest().setAttribute("queryPageThesises", thesises);
    }
    String text = QueryPageUtils.getSetting(queryPage, "thesis.text");
    String description = QueryPageUtils.getSetting(queryPage, "thesis.description");
    if (!StringUtils.isEmpty(text) || !StringUtils.isEmpty(description)) {
      thesises.put(queryPage.getId(), new QueryReportPageThesis(text, description));
    }
  }
  
  public static void appendQueryPageReplys(RequestContext requestContext, Long queryPageId, List<QueryReply> pageReplys) {
    @SuppressWarnings("unchecked")
    Map<Long, List<QueryReply>> requestPageComments = (Map<Long, List<QueryReply>>) requestContext.getRequest().getAttribute("queryPageReplys");
    
    if (requestPageComments == null) {
      requestPageComments = new HashMap<Long, List<QueryReply>>();
      requestContext.getRequest().setAttribute("queryPageReplys", requestPageComments);
    }
    
    requestPageComments.put(queryPageId, pageReplys);
  }
  
  public static void appendQueryPageRootComments(RequestContext requestContext, Long queryPageId, List<QueryQuestionComment> pageComments) {
    @SuppressWarnings("unchecked")
    Map<Long, List<QueryQuestionComment>> requestPageComments = (Map<Long, List<QueryQuestionComment>>) requestContext.getRequest().getAttribute("queryPageComments");
    
    if (requestPageComments == null) {
      requestPageComments = new HashMap<Long, List<QueryQuestionComment>>();
      requestContext.getRequest().setAttribute("queryPageComments", requestPageComments);
    }
    
    requestPageComments.put(queryPageId, pageComments);
  }
  
  public static void appendQueryPageChildComments(RequestContext requestContext, Long parentCommentId, List<QueryQuestionComment> pageComments) {
    @SuppressWarnings("unchecked")
    Map<Long, List<QueryQuestionComment>> requestPageComments = (Map<Long, List<QueryQuestionComment>>) requestContext.getRequest().getAttribute("queryPageCommentChildren");
    
    if (requestPageComments == null) {
      requestPageComments = new HashMap<Long, List<QueryQuestionComment>>();
      requestContext.getRequest().setAttribute("queryPageCommentChildren", requestPageComments);
    }
    
    requestPageComments.put(parentCommentId, pageComments);
  }

  public static void appendQueryPageChildComments(RequestContext requestContext, Map<Long, List<QueryQuestionComment>> childComments) {
    @SuppressWarnings("unchecked")
    Map<Long, List<QueryQuestionComment>> requestPageComments = (Map<Long, List<QueryQuestionComment>>) requestContext.getRequest().getAttribute("queryPageCommentChildren");
    
    if (requestPageComments == null) {
      requestPageComments = new HashMap<Long, List<QueryQuestionComment>>();
      requestContext.getRequest().setAttribute("queryPageCommentChildren", requestPageComments);
    }
    
    requestPageComments.putAll(childComments);
  }
  
  /**
   * Loads the whole comment tree and appends it to requestContext for jsp to read.
   * 
   * @param requestContext
   * @param queryPage
   * @param queryReplies 
   */
  public static void appendQueryPageComments(RequestContext requestContext, QueryPage queryPage) {
    QueryQuestionCommentDAO queryQuestionCommentDAO = new QueryQuestionCommentDAO();
    PanelStamp activeStamp = RequestUtils.getActiveStamp(requestContext);
    List<QueryQuestionComment> rootComments = queryQuestionCommentDAO.listRootCommentsByQueryPageAndStamp(queryPage, activeStamp);
    Map<Long, List<QueryQuestionComment>> childComments = queryQuestionCommentDAO.listTreesByQueryPageAndStamp(queryPage, activeStamp);

    QueryUtils.appendQueryPageRootComments(requestContext, queryPage.getId(), rootComments);
    QueryUtils.appendQueryPageChildComments(requestContext, childComments);
    
    int commentCount = rootComments.size();
    
    for (Object key : childComments.keySet()) {
      List<QueryQuestionComment> childCommentList = childComments.get(key);
      if (childCommentList != null)
        commentCount += childCommentList.size();
    }
    
    requestContext.getRequest().setAttribute("queryPageCommentCount", commentCount);
  }
  
  public static Query copyQuery(RequestContext requestContext, Query query, String newName, Panel targetPanel, boolean copyAnswers, boolean copyComments) {
    User user = RequestUtils.getUser(requestContext);
    
    // Data access objects

    QueryDAO queryDAO = new QueryDAO();
    QueryPageDAO queryPageDAO = new QueryPageDAO();
    QueryFieldDAO queryFieldDAO = new QueryFieldDAO();
    QueryReplyDAO queryReplyDAO = new QueryReplyDAO();
    QuerySectionDAO querySectionDAO = new QuerySectionDAO();
    QueryPageSettingDAO queryPageSettingDAO = new QueryPageSettingDAO();
    QueryNumericFieldDAO queryNumericFieldDAO = new QueryNumericFieldDAO();
    QueryOptionFieldDAO queryOptionFieldDAO = new QueryOptionFieldDAO();
    QueryOptionFieldOptionDAO queryOptionFieldOptionDAO = new QueryOptionFieldOptionDAO();
    QueryOptionFieldOptionGroupDAO queryOptionFieldOptionGroupDAO = new QueryOptionFieldOptionGroupDAO();
    QueryScaleFieldDAO queryScaleFieldDAO = new QueryScaleFieldDAO();
    QueryTextFieldDAO queryTextFieldDAO = new QueryTextFieldDAO();
    QueryQuestionTextAnswerDAO queryQuestionTextAnswerDAO = new QueryQuestionTextAnswerDAO();
    QueryQuestionNumericAnswerDAO queryQuestionNumericAnswerDAO = new QueryQuestionNumericAnswerDAO();
    QueryQuestionCommentDAO queryQuestionCommentDAO = new QueryQuestionCommentDAO();
    QueryQuestionOptionAnswerDAO queryQuestionOptionAnswerDAO = new QueryQuestionOptionAnswerDAO();
    QueryQuestionMultiOptionAnswerDAO queryQuestionMultiOptionAnswerDAO = new QueryQuestionMultiOptionAnswerDAO();
    QueryQuestionOptionGroupOptionAnswerDAO queryQuestionOptionGroupOptionAnswerDAO = new QueryQuestionOptionGroupOptionAnswerDAO();
    
    // Comments are tied to answers
    
    if (!copyAnswers && copyComments) {
      copyAnswers = copyComments;
    }
    
    // Queries containing expertise pages cannot be copied to other panels (due to differing expertises)
    
    Panel sourcePanel = ResourceUtils.getResourcePanel(query); 
    List<QueryPage> expertisePages = queryPageDAO.listByQueryAndType(query, QueryPageType.EXPERTISE); 
    if (!expertisePages.isEmpty() && !sourcePanel.getId().equals(targetPanel.getId())) {
      Messages messages = Messages.getInstance();
      Locale locale = requestContext.getRequest().getLocale();
      throw new SmvcRuntimeException(EdelfoiStatusCode.CANNOT_COPY_EXPERTISE_QUERY, messages.getText(locale, "exception.1030.cannotCopyExpertiseQuery"));
    }
    
    // Query
    
    String urlName = ResourceUtils.getUrlName(newName);
    if (!ResourceUtils.isUrlNameAvailable(urlName, targetPanel.getRootFolder())) {
      Messages messages = Messages.getInstance();
      Locale locale = requestContext.getRequest().getLocale();
      throw new SmvcRuntimeException(EdelfoiStatusCode.DUPLICATE_RESOURCE_NAME, messages.getText(locale, "exception.1005.resourceNameInUse"));
    }
    
    Integer indexNumber = ResourceUtils.getNextIndexNumber(targetPanel.getRootFolder());
    Query newQuery = queryDAO.create(
        targetPanel.getRootFolder(),
        newName,
        urlName,
        query.getAllowEditReply(),
        query.getDescription(),
        query.getState(),
        query.getCloses(),
        indexNumber,
        query.getCreator(),
        query.getCreated(),
        query.getLastModifier(),
        query.getLastModified());
    
    // Replies
    
    HashMap<Long, QueryReply> replyMap = null;
    List<QueryReply> queryReplies = null;
    if (copyAnswers) {
      replyMap = new HashMap<Long, QueryReply>();
      if (sourcePanel.getId().equals(targetPanel.getId())) {
        
        // When copying within the same panel, copy all replies of all stamps
        
        queryReplies = queryReplyDAO.listByQueryAndArchived(query, Boolean.FALSE);
        for (QueryReply queryReply : queryReplies) {
          QueryReply newReply = queryReplyDAO.create(
              queryReply.getUser(),
              newQuery,
              queryReply.getStamp(),
              queryReply.getComplete(),
              queryReply.getCreator(),
              queryReply.getCreated(),
              queryReply.getLastModifier(),
              queryReply.getLastModified());
          replyMap.put(queryReply.getId(), newReply);
        }
      }
      else {
        
        // When copying between panels, only copy the replies of the latest source panel stamp to the latest target panel stamp 
        
        queryReplies = queryReplyDAO.listByQueryAndStampAndArchived(query, sourcePanel.getCurrentStamp(), Boolean.FALSE);
        for (QueryReply queryReply : queryReplies) {
          QueryReply newReply = queryReplyDAO.create(
              queryReply.getUser(),
              newQuery,
              targetPanel.getCurrentStamp(),
              queryReply.getComplete(),
              queryReply.getCreator(),
              queryReply.getCreated(),
              queryReply.getLastModifier(),
              queryReply.getLastModified());
          replyMap.put(queryReply.getId(), newReply);
        }
      }
    }

    // Special handling for collage pages, Part I :/
    
    List<QueryPage> collagePages = new ArrayList<QueryPage>();
    HashMap<String, Long> pageIds = new HashMap<String, Long>();

    // Sections
    
    List<QuerySection> querySections = querySectionDAO.listByQuery(query);
    for (QuerySection querySection : querySections) {
      QuerySection newQuerySection = querySectionDAO.create(user, newQuery, querySection.getTitle(), querySection.getSectionNumber(), querySection.getVisible(), querySection.getCommentable(), querySection.getViewDiscussions());
    
      // Pages and page settings
      
      List<QueryPage> queryPages = queryPageDAO.listByQuerySection(querySection);
      for (QueryPage queryPage : queryPages) {
        QueryPage newQueryPage = queryPageDAO.create(user, newQuerySection, queryPage.getPageType(), queryPage.getPageNumber(), queryPage.getTitle(), queryPage.getVisible());
        List<QueryPageSetting> queryPageSettings = queryPageSettingDAO.listByQueryPage(queryPage);
        for (QueryPageSetting queryPageSetting : queryPageSettings) {
          queryPageSettingDAO.create(queryPageSetting.getKey(), newQueryPage, queryPageSetting.getValue());
        }
        
        // Special handling for collage pages, Part II :/
        
        pageIds.put(queryPage.getId().toString(), newQueryPage.getId());
        if (queryPage.getPageType() == QueryPageType.COLLAGE_2D) {
          collagePages.add(newQueryPage);
        }
      
        // Comments
        
        if (copyComments) {
          HashMap<Long, Long> commentMap = new HashMap<Long, Long>();
          List<QueryQuestionComment> queryComments;
          if (sourcePanel.getId().equals(targetPanel.getId())) {
            queryComments = queryQuestionCommentDAO.listByQueryPage(queryPage); 
          }
          else {
            queryComments = queryQuestionCommentDAO.listByQueryPageAndStamp(queryPage, sourcePanel.getCurrentStamp());
          }
          Collections.sort(queryComments, new Comparator<QueryQuestionComment>() {
            @Override
            public int compare(QueryQuestionComment o1, QueryQuestionComment o2) {
              return o1.getId().compareTo(o2.getId());
            }
          });
          for (QueryQuestionComment queryComment : queryComments) {
            QueryReply newReply = replyMap.get(queryComment.getQueryReply().getId());
            QueryQuestionComment parentComment = queryComment.getParentComment() == null ? null : queryQuestionCommentDAO.findById(commentMap.get(queryComment.getParentComment().getId()));
            QueryQuestionComment newComment = queryQuestionCommentDAO.create(
                newReply,
                newQueryPage,
                parentComment,
                queryComment.getComment(),
                queryComment.getHidden(),
                queryComment.getCreator(),
                queryComment.getCreated(),
                queryComment.getLastModifier(),
                queryComment.getLastModified());
            commentMap.put(queryComment.getId(), newComment.getId());
          }
        }
        
        // Fields and (optionally) answers
        
        List<QueryField> queryFields = queryFieldDAO.listByQueryPage(queryPage);
        for (QueryField queryField : queryFields) {
          QueryField newQueryField = null;
          switch (queryField.getType()) {
          
            // Text fields
          
            case TEXT:
              newQueryField = queryTextFieldDAO.create(newQueryPage, queryField.getName(), queryField.getMandatory(), queryField.getCaption());
              if (copyAnswers) {
                for (QueryReply queryReply : queryReplies) {
                  QueryQuestionTextAnswer answer = queryQuestionTextAnswerDAO.findByQueryReplyAndQueryField(queryReply, queryField);
                  if (answer != null) {
                    QueryReply newQueryReply = replyMap.get(queryReply.getId());
                    queryQuestionTextAnswerDAO.create(newQueryReply, newQueryField, answer.getData());
                  }
                }
              }
              break;

            // Option fields

            case OPTIONFIELD:
              QueryOptionField optionField = (QueryOptionField) queryField;
              newQueryField = queryOptionFieldDAO.create(newQueryPage, optionField.getName(), optionField.getMandatory(), optionField.getCaption());
              List<QueryOptionFieldOption> options = queryOptionFieldOptionDAO.listByQueryField(optionField);
              Map<Long, Long> optionMap = new HashMap<Long, Long>();
              for (QueryOptionFieldOption option : options) {
                QueryOptionFieldOption newOption = queryOptionFieldOptionDAO.create((QueryOptionField) newQueryField, option.getText(), option.getValue());
                optionMap.put(option.getId(), newOption.getId());
              }
              Map<Long, QueryOptionFieldOptionGroup> optionGroupMap = new HashMap<Long, QueryOptionFieldOptionGroup>();
              List<QueryOptionFieldOptionGroup> groups = queryOptionFieldOptionGroupDAO.listByQueryField(optionField);
              for (QueryOptionFieldOptionGroup group : groups) {
                QueryOptionFieldOptionGroup newGroup = queryOptionFieldOptionGroupDAO.create((QueryOptionField) newQueryField, group.getName());
                optionGroupMap.put(group.getId(), newGroup);
              }
              if (copyAnswers) {
                for (QueryReply queryReply : queryReplies) {
                  QueryQuestionMultiOptionAnswer multiAnswer = queryQuestionMultiOptionAnswerDAO.findByQueryReplyAndQueryField(queryReply, queryField);
                  if (multiAnswer != null) {
                    // QueryQuestionMultiOptionAnswer
                    QueryReply newQueryReply = replyMap.get(queryReply.getId());
                    Set<QueryOptionFieldOption> newOptions = new HashSet<QueryOptionFieldOption>();
                    for (QueryOptionFieldOption option : multiAnswer.getOptions()) {
                      QueryOptionFieldOption newOption = queryOptionFieldOptionDAO.findById(optionMap.get(option.getId()));
                      newOptions.add(newOption);
                    }
                    queryQuestionMultiOptionAnswerDAO.create(newQueryReply, newQueryField, newOptions);
                  }
                  else {
                    // QueryQuestionOptionGroupOptionAnswer
                    List<QueryQuestionOptionGroupOptionAnswer> groupAnswers = queryQuestionOptionGroupOptionAnswerDAO.listByQueryReplyAndQueryField(queryReply, queryField);
                    if (!groupAnswers.isEmpty()) {
                      for (QueryQuestionOptionGroupOptionAnswer groupAnswer : groupAnswers) {
                        QueryReply newQueryReply = replyMap.get(queryReply.getId());
                        QueryOptionFieldOption newOption = queryOptionFieldOptionDAO.findById(optionMap.get(groupAnswer.getOption().getId()));
                        QueryOptionFieldOptionGroup newGroup = optionGroupMap.get(groupAnswer.getGroup().getId());
                        queryQuestionOptionGroupOptionAnswerDAO.create(newQueryReply, newQueryField, newOption, newGroup);
                      }
                    }
                    else {
                      // QueryQuestionOptionAnswer
                      List<QueryQuestionOptionAnswer> optionAnswers = queryQuestionOptionAnswerDAO.listByQueryReplyAndQueryField(queryReply, queryField);
                      for (QueryQuestionOptionAnswer optionAnswer : optionAnswers) {
                        QueryOptionFieldOption newOption = queryOptionFieldOptionDAO.findById(optionMap.get(optionAnswer.getOption().getId()));
                        QueryReply newQueryReply = replyMap.get(queryReply.getId());
                        queryQuestionOptionAnswerDAO.create(newQueryReply, newQueryField, newOption);
                      }
                    }
                  }
                }
              }
              break;
              
            // Numeric scale fields

            case NUMERIC_SCALE:
              QueryScaleField scaleField = (QueryScaleField) queryField;
              newQueryField = queryScaleFieldDAO.create(newQueryPage, scaleField.getName(), scaleField.getMandatory(), scaleField.getCaption(),
                  scaleField.getMin(), scaleField.getMax(), scaleField.getPrecision(), scaleField.getStep());
              if (copyAnswers) {
                for (QueryReply queryReply : queryReplies) {
                  QueryQuestionNumericAnswer answer = queryQuestionNumericAnswerDAO.findByQueryReplyAndQueryField(queryReply, queryField);
                  if (answer != null) {
                    QueryReply newQueryReply = replyMap.get(queryReply.getId());
                    queryQuestionNumericAnswerDAO.create(newQueryReply, newQueryField, answer.getData());
                  }
                }
              }
              break;
              
            // Numeric fields
              
            case NUMERIC:
              QueryNumericField numericField = (QueryNumericField) queryField;
              newQueryField = queryNumericFieldDAO.create(newQueryPage, numericField.getName(), numericField.getMandatory(),
                  numericField.getCaption(), numericField.getMin(), numericField.getMax(), numericField.getPrecision());
              if (copyAnswers) {
                for (QueryReply queryReply : queryReplies) {
                  QueryQuestionNumericAnswer answer = queryQuestionNumericAnswerDAO.findByQueryReplyAndQueryField(queryReply, queryField);
                  if (answer != null) {
                    QueryReply newQueryReply = replyMap.get(queryReply.getId());
                    queryQuestionNumericAnswerDAO.create(newQueryReply, newQueryField, answer.getData());
                  }
                }
              }
              break;
          }
        }
      }
    }

    // Special handling for collage pages, Part III :/
    
    if (!collagePages.isEmpty()) {
      QueryPageSettingKeyDAO queryPageSettingKeyDAO = new QueryPageSettingKeyDAO();
      for (QueryPage collagePage : collagePages) {

        // Included pages
        
        QueryPageSettingKey key = queryPageSettingKeyDAO.findByName("collage2d.includedPages");
        QueryPageSetting includedPageSetting = queryPageSettingDAO.findByKeyAndQueryPage(key, collagePage);
        if (includedPageSetting != null) {
          String[] includedPages = includedPageSetting.getValue().split("&");
          for (int i = 0; i < includedPages.length; i++) {
            includedPages[i] = pageIds.get(includedPages[i]).toString();
          }
          if (includedPages.length > 0) {
            queryPageSettingDAO.updateValue(includedPageSetting, StringUtils.join(includedPages, '&'));
          }
        }
        
        // Included page settings
        
        key = queryPageSettingKeyDAO.findByName("collage2d.pageSettings");
        QueryPageSetting pageSettingsSetting = queryPageSettingDAO.findByKeyAndQueryPage(key, collagePage);
        if (pageSettingsSetting != null) {
          String[] pageSettings = pageSettingsSetting.getValue().split("&");
          for (int i = 0; i < pageSettings.length; i++) {
            int eqPos = pageSettings[i].indexOf('=');
            pageSettings[i] = pageIds.get(pageSettings[i].substring(0, eqPos)) + pageSettings[i].substring(eqPos);
          }
          if (pageSettings.length > 0) {
            queryPageSettingDAO.updateValue(pageSettingsSetting, StringUtils.join(pageSettings, '&'));
          }
        }
      }
    }
    
    return newQuery;
  }

  public static void stampQuery(Query query, PanelStamp sourceStamp, PanelStamp targetStamp) {
    
    // Data access objects

    QueryPageDAO queryPageDAO = new QueryPageDAO();
    QueryFieldDAO queryFieldDAO = new QueryFieldDAO();
    QueryReplyDAO queryReplyDAO = new QueryReplyDAO();
    QuerySectionDAO querySectionDAO = new QuerySectionDAO();
    QueryQuestionTextAnswerDAO queryQuestionTextAnswerDAO = new QueryQuestionTextAnswerDAO();
    QueryQuestionNumericAnswerDAO queryQuestionNumericAnswerDAO = new QueryQuestionNumericAnswerDAO();
    QueryQuestionCommentDAO queryQuestionCommentDAO = new QueryQuestionCommentDAO();
    QueryQuestionOptionAnswerDAO queryQuestionOptionAnswerDAO = new QueryQuestionOptionAnswerDAO();
    QueryQuestionMultiOptionAnswerDAO queryQuestionMultiOptionAnswerDAO = new QueryQuestionMultiOptionAnswerDAO();
    QueryQuestionOptionGroupOptionAnswerDAO queryQuestionOptionGroupOptionAnswerDAO = new QueryQuestionOptionGroupOptionAnswerDAO();
    
    // Replies
    
    Map<Long, QueryReply> replyMap = new HashMap<Long, QueryReply>();
    List<QueryReply> queryReplies = null;
    queryReplies = queryReplyDAO.listByQueryAndStamp(query, sourceStamp);
    for (QueryReply queryReply : queryReplies) {
      QueryReply newReply = queryReplyDAO.create(
          queryReply.getUser(),
          queryReply.getQuery(),
          targetStamp,
          queryReply.getComplete(),
          queryReply.getCreator(),
          queryReply.getCreated(),
          queryReply.getLastModifier(),
          queryReply.getLastModified());
      replyMap.put(queryReply.getId(), newReply);
    }

    // Sections
    
    List<QuerySection> querySections = querySectionDAO.listByQuery(query);
    for (QuerySection querySection : querySections) {
    
      // Pages
      
      List<QueryPage> queryPages = queryPageDAO.listByQuerySection(querySection);
      for (QueryPage queryPage : queryPages) {
      
        // Comments
        
        Map<Long, QueryQuestionComment> commentMap = new HashMap<Long, QueryQuestionComment>();
        List<QueryQuestionComment> queryComments = queryQuestionCommentDAO.listByQueryPageAndStamp(queryPage, sourceStamp);
        Collections.sort(queryComments, new Comparator<QueryQuestionComment>() {
          @Override
          public int compare(QueryQuestionComment o1, QueryQuestionComment o2) {
            return o1.getId().compareTo(o2.getId());
          }
        });
        for (QueryQuestionComment queryComment : queryComments) {
          QueryReply newReply = replyMap.get(queryComment.getQueryReply().getId());
          QueryQuestionComment parentComment = queryComment.getParentComment() == null ? null : commentMap.get(queryComment.getParentComment().getId());
          QueryQuestionComment newComment = queryQuestionCommentDAO.create(
              newReply,
              queryPage,
              parentComment,
              queryComment.getComment(),
              queryComment.getHidden(),
              queryComment.getCreator(),
              queryComment.getCreated(),
              queryComment.getLastModifier(),
              queryComment.getLastModified());
          commentMap.put(queryComment.getId(), newComment);
        }
        
        // Field answers
        
        List<QueryField> queryFields = queryFieldDAO.listByQueryPage(queryPage);
        for (QueryField queryField : queryFields) {
          switch (queryField.getType()) {
          
            // Text fields
          
            case TEXT:
              for (QueryReply queryReply : queryReplies) {
                QueryQuestionTextAnswer answer = queryQuestionTextAnswerDAO.findByQueryReplyAndQueryField(queryReply, queryField);
                if (answer != null) {
                  queryQuestionTextAnswerDAO.create(replyMap.get(queryReply.getId()), queryField, answer.getData());
                }
              }
              break;

            // Option fields

            case OPTIONFIELD:

              if (!queryReplies.isEmpty()) {
                QueryPageType pageType = queryPage.getPageType();
                switch (pageType) {
                  case THESIS_GROUPING:
                    HashMap<Long, List<QueryQuestionOptionGroupOptionAnswer>> groupOptionAnswersByReplies = null;
                    List<QueryQuestionOptionGroupOptionAnswer> allGroupOptionAnswers = queryQuestionOptionGroupOptionAnswerDAO.listByQueryRepliesAndQueryField(queryReplies, queryField);
                    if (!allGroupOptionAnswers.isEmpty()) {
                      groupOptionAnswersByReplies = new HashMap<Long, List<QueryQuestionOptionGroupOptionAnswer>>();
                      for (QueryQuestionOptionGroupOptionAnswer optionAnswer : allGroupOptionAnswers) {
                        List<QueryQuestionOptionGroupOptionAnswer> singleReplyAnswers = groupOptionAnswersByReplies.get(optionAnswer.getQueryReply().getId());
                        if (singleReplyAnswers == null) {
                          singleReplyAnswers = new ArrayList<QueryQuestionOptionGroupOptionAnswer>();
                          groupOptionAnswersByReplies.put(optionAnswer.getQueryReply().getId(), singleReplyAnswers);
                        }
                        singleReplyAnswers.add(optionAnswer);
                      }
                      for (QueryReply queryReply : queryReplies) {
                        List<QueryQuestionOptionGroupOptionAnswer> groupAnswers = groupOptionAnswersByReplies.get(queryReply.getId());
                        if (groupAnswers != null && !groupAnswers.isEmpty()) {
                          for (QueryQuestionOptionGroupOptionAnswer groupAnswer : groupAnswers) {
                            queryQuestionOptionGroupOptionAnswerDAO.create(replyMap.get(queryReply.getId()), queryField, groupAnswer.getOption(), groupAnswer.getGroup());
                          }
                        }
                      }
                    }
                    break;
                  case THESIS_MULTI_SELECT:
                  case EXPERTISE:
                    HashMap<Long, QueryQuestionMultiOptionAnswer> multiOptionAnswersByReplies = null;
                    List<QueryQuestionMultiOptionAnswer> allMultiOptionAnswers = queryQuestionMultiOptionAnswerDAO.listByQueryRepliesAndQueryField(queryReplies, queryField);
                    if (!allMultiOptionAnswers.isEmpty()) {
                      multiOptionAnswersByReplies = new HashMap<Long, QueryQuestionMultiOptionAnswer>();
                      for (QueryQuestionMultiOptionAnswer optionAnswer : allMultiOptionAnswers) {
                        multiOptionAnswersByReplies.put(optionAnswer.getQueryReply().getId(), optionAnswer);
                      }
                      for (QueryReply queryReply : queryReplies) {
                        QueryQuestionMultiOptionAnswer multiAnswer = multiOptionAnswersByReplies.get(queryReply.getId());
                        if (multiAnswer != null) {
                          Set<QueryOptionFieldOption> options = new HashSet<QueryOptionFieldOption>();
                          options.addAll(multiAnswer.getOptions());
                          queryQuestionMultiOptionAnswerDAO.create(replyMap.get(queryReply.getId()), queryField, options);
                        }
                      }
                    }
                    break;
                  default:
                    HashMap<Long, List<QueryQuestionOptionAnswer>> optionAnswersByReplies = null;
                    List<QueryQuestionOptionAnswer> allOptionAnswers = queryQuestionOptionAnswerDAO.listByQueryRepliesAndQueryField(queryReplies, queryField);
                    if (!allOptionAnswers.isEmpty()) {
                      optionAnswersByReplies = new HashMap<Long, List<QueryQuestionOptionAnswer>>(); 
                      for (QueryQuestionOptionAnswer optionAnswer : allOptionAnswers) {
                        List<QueryQuestionOptionAnswer> singleReplyAnswers = optionAnswersByReplies.get(optionAnswer.getQueryReply().getId());
                        if (singleReplyAnswers == null) {
                          singleReplyAnswers = new ArrayList<QueryQuestionOptionAnswer>();
                          optionAnswersByReplies.put(optionAnswer.getQueryReply().getId(), singleReplyAnswers);
                        }
                        singleReplyAnswers.add(optionAnswer);
                      }
                      for (QueryReply queryReply : queryReplies) {
                        if (optionAnswersByReplies != null) {
                          List<QueryQuestionOptionAnswer> optionAnswers = optionAnswersByReplies.get(queryReply.getId());
                          if (optionAnswers != null && !optionAnswers.isEmpty()) {
                            for (QueryQuestionOptionAnswer optionAnswer : optionAnswers) {
                              queryQuestionOptionAnswerDAO.create(replyMap.get(queryReply.getId()), queryField, optionAnswer.getOption());
                            }
                          }
                        }
                      }
                    }
                    break;
                }
              }
             
              break;
              
            // Numeric scale fields

            case NUMERIC_SCALE:
            case NUMERIC:
              for (QueryReply queryReply : queryReplies) {
                QueryQuestionNumericAnswer answer = queryQuestionNumericAnswerDAO.findByQueryReplyAndQueryField(queryReply, queryField);
                if (answer != null) {
                  queryQuestionNumericAnswerDAO.create(replyMap.get(queryReply.getId()), queryField, answer.getData());
                }
              }
              break;
            default:
              throw new IllegalArgumentException("Unsupported query field type: " + queryField.getType());
          }
        }
      }
    }
  }

}
