/* ========================================================================
 * Copyright (c) 2005-2020 The OPC Foundation, Inc. All rights reserved.
 *
 * OPC Foundation MIT License 1.00
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 *
 * The complete license agreement can be found here:
 * http://opcfoundation.org/License/MIT/1.00/
 * ======================================================================*/

using System;

namespace Opc.Ua.Server
{
    /// <summary>
    /// Describes the properties of a server reverse connection.
    /// </summary>
    public class ReverseConnectProperty
    {
        /// <summary>
        /// Initialize a reverse connect server property.
        /// </summary>
        /// <param name="clientUrl">The Url of the reverse connect client.</param>
        /// <param name="timeout">The timeout to use for a reverse connect attempt.</param>
        /// <param name="maxSessionCount">The maximum number of sessions allowed to the client.</param>
        /// <param name="configEntry">If this is an application configuration entry.</param>
        /// <param name="enabled">If the connection is enabled.</param>
        public ReverseConnectProperty(
            Uri clientUrl,
            int timeout,
            int maxSessionCount,
            bool configEntry,
            bool enabled = true)
        {
            ClientUrl = clientUrl;
            Timeout = timeout > 0 ? timeout : ReverseConnectServer.DefaultReverseConnectTimeout;
            MaxSessionCount = maxSessionCount;
            ConfigEntry = configEntry;
            Enabled = enabled;
        }

        /// <summary>
        /// The Url of the reverse connect client.
        /// </summary>
        public readonly Uri ClientUrl;

        /// <summary>
        /// The timeout to use for a reverse connect attempt.
        /// </summary>
        public readonly int Timeout;

        /// <summary>
        /// If this is an application configuration entry.
        /// </summary>
        public readonly bool ConfigEntry;

        /// <summary>
        /// The service result of the last connection attempt.
        /// </summary>
        public ServiceResult ServiceResult;

        /// <summary>
        /// The last state of the reverse connection.
        /// </summary>
        public ReverseConnectState LastState = ReverseConnectState.Closed;

        /// <summary>
        /// The maximum number of sessions allowed to the client.
        /// </summary>
        public int MaxSessionCount;

        /// <summary>
        /// If the connection is enabled.
        /// </summary>
        public bool Enabled;

        /// <summary>
        /// The time when the connection was rejected.
        /// </summary>
        public DateTime RejectTime;
    }
}
