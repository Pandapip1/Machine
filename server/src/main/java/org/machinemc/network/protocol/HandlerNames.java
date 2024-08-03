/*
 * This file is part of Machine.
 *
 * Machine is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * Machine is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Machine.
 * If not, see https://www.gnu.org/licenses/.
 */
package org.machinemc.network.protocol;

/**
 * Constants for names of different handlers used in
 * the client's channel pipeline.
 */
public interface HandlerNames {

    String COMPRESSION_DECODER = "compression_decoder";
    String COMPRESSION_ENCODER = "compression_encoder";

    String ENCRYPTION_DECODER = "encryption_decoder";
    String ENCRYPTION_ENCODER = "encryption_encoder";

    String LEGACY_PING_DECODER = "legacy_ping_decoder";
    String LEGACY_PING_ENCODER = "legacy_ping_encoder";

    String LENGTH_DECODER = "length_decoder";
    String LENGTH_ENCODER = "length_encoder";

    String PACKET_DECODER = "packet_decoder";
    String PACKET_ENCODER = "packet_encoder";

    String PACKET_HANDLER = "packet_handler";

}
