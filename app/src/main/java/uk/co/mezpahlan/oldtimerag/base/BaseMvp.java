package uk.co.mezpahlan.oldtimerag.base;

/**
 * Base MVP definitions for operations that all implementations will use.
 */
public interface BaseMvp {

    interface LCEView <VT> {
        void showLoading();
        void showContent();
        void showError();
        void updateContent(VT viewType);
    }

    interface Presenter <PT> {
        void load();
        void onLoadSuccess(PT presenterType);
        void onLoadError();
        void onDestroy(boolean isConfigChanging);
    }

    interface ModelInteractor <MT> {
        void fetch();
        void onFetched(MT modelType);
        void onError();
        void onDestroy();
    }
}