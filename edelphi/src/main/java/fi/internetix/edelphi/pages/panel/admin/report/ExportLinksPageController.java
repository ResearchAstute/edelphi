package fi.internetix.edelphi.pages.panel.admin.report;

import fi.internetix.edelphi.DelfoiActionName;
import fi.internetix.edelphi.domainmodel.actions.DelfoiActionScope;
import fi.internetix.edelphi.pages.panel.PanelPageController;
import fi.internetix.smvc.controllers.PageRequestContext;

public class ExportLinksPageController extends PanelPageController {

  public ExportLinksPageController() {
    setAccessAction(DelfoiActionName.MANAGE_PANEL_REPORTS, DelfoiActionScope.PANEL);
  }
  
  @Override
  public void processPageRequest(PageRequestContext pageRequestContext) {
    pageRequestContext.setIncludeJSP("/jsp/blocks/panel/admin/report/exportlinks.jsp");
  }

}
