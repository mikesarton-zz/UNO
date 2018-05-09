
package esi.atl.g41864.uno.messages;

import java.io.Serializable;

/**
 *
 * @author mike
 */
public enum MessagesTypes implements Serializable {
    CLI_CONNECT,
    CLI_DISCONNECT,
    CLI_RECONNECT,
    CLI_PLAYCARD,
    CLI_PICKCARD,
    CLI_RESTART,
    CLI_CHANGESCORE,
    SRV_RESPONSETORECONNECT,
    SRV_EXCEPTION,
    SRV_GAMEOVER
}
